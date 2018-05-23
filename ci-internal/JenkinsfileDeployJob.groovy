@Library('surf-lib') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.EmptyPipeline
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.stage.body.CommonAndroidStages
import ru.surfstudio.ci.JarvisUtil
import ru.surfstudio.ci.CommonUtil
import ru.surfstudio.ci.NodeProvider

import static ru.surfstudio.ci.CommonUtil.applyParameterIfNotEmpty

//Кастомный пайплайн для деплоя артефактов

// Имена Шагов
def INIT = 'Init'
def PRE_MERGE = 'Checkout'
def BUILD = 'Build'
def UNIT_TEST = 'Unit Test'
def INSTRUMENTATION_TEST = 'Instrumentation Test'
def STATIC_CODE_ANALYSIS = 'Static Code Analysis'
def DEPLOY = 'Deploy'

//init
def script = this
def pipeline = new EmptyPipeline(script)
def branchName = ""
pipeline.init()

//configuration
pipeline.node = NodeProvider.getAndroidNode()

pipeline.stages = [
        pipeline.createStage(INIT, StageStrategy.FAIL_WHEN_STAGE_ERROR){
        
            CommonUtil.printInitialStageStrategies(pipeline)

            script.echo "artifactory user: ${script.env.surf_maven_username}"

            //Выбираем значения веток из параметров, Установка их в параметры происходит
            // если триггером был webhook или если стартанули Job вручную
            //Используется имя branchName_0 из за особенностей jsonPath в GenericWebhook plugin
            applyParameterIfNotEmpty(script, 'branchName', script.params.branchName_0, {
                value -> branchName = value
            })
        
            if(branchName.contains("project-snapshot")){
                script.echo "Apply lightweight strategies for project-snapshot branch"
                pipeline.getStage(UNIT_TEST).strategy = SKIP_STAGE
                pipeline.getStage(INSTRUMENTATION_TEST).strategy = SKIP_STAGE
                pipeline.getStage(STATIC_CODE_ANALYSIS).strategy = SKIP_STAGE
            }

            CommonUtil.abortDuplicateBuilds(script, branchName)
        },
        pipeline.createStage(CHECKOUT, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            script.checkout([
                    $class                           : 'GitSCM',
                    branches                         : [[name: "${branchName}"]],
                    doGenerateSubmoduleConfigurations: script.scm.doGenerateSubmoduleConfigurations,
                    userRemoteConfigs                : script.scm.userRemoteConfigs,
            ])
        },
        pipeline.createStage(BUILD, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            CommonAndroidStages.buildStageBodyAndroid(script, "clean assemble")
        },
        pipeline.createStage(UNIT_TEST, StageStrategy.FAIL_WHEN_STAGE_ERROR){
            CommonAndroidStages.unitTestStageBodyAndroid(script,
                            "testReleaseUnitTest",
                            "**/test-results/testReleaseUnitTest/*.xml",
                            "app/build/reports/tests/testReleaseUnitTest/")
        },
        pipeline.createStage(INSTRUMENTATION_TEST, StageStrategy.SKIP_STAGE) {
            CommonAndroidStages.instrumentationTestStageBodyAndroid(script,
                    "connectedAndroidTest",
                    "**/outputs/androidTest-results/connected/*.xml",
                    "app/build/reports/androidTests/connected/")
        },
        createStage(STATIC_CODE_ANALYSIS, StageStrategy.SKIP_STAGE) {
            CommonAndroidStages.staticCodeAnalysisStageBody(script)
        },
        createStage(DEPLOY, StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            //script.sh "./gradlew clean uploadArchives"
        }
]

pipeline.finalizeBody = {
    def jenkinsLink = CommonUtil.getBuildUrlHtmlLink(script)
    def message
    def success = pipeline.jobResult != Result.SUCCESS
    if (success) {
        def unsuccessReasons = CommonUtil.unsuccessReasonsToString(pipeline.stages)
        message = "Deploy ветки ${branchName} не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
    } else {
        message = "Deploy ветки ${branchName} успешно выполнен. ${jenkinsLink}"
    }
    JarvisUtil.sendMessageToGroup(script, message, script.scm.userRemoteConfigs[0].url, "bitbucket", success)
}

pipeline.run()
