@Library('surf-lib@version-1.0.0-SNAPSHOT') // https://bitbucket.org/surfstudio/jenkins-pipeline-lib/
import ru.surfstudio.ci.pipeline.empty.EmptyScmPipeline
import ru.surfstudio.ci.stage.StageStrategy
import ru.surfstudio.ci.NodeProvider

//init
def pipeline = new EmptyScmPipeline(this)

//configuration
def mirrorRepoCredentialID = "76dbac13-e6ea-4ed0-a013-e06cad01be2d"

pipeline.node = NodeProvider.getAndroidNode()

pipeline.propertiesProvider = {
    return [
        pipelineTriggers([
            GenericTrigger(),
            pollSCM('')
        ])
    ]
}

//stages
pipeline.stages = [
        pipeline.createStage("CLONE", StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            sh "rm -rf android-standard"
            withCredentials([usernamePassword(credentialsId: pipeline.repoCredentialsId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                echo "credentialsId: $pipeline.repoCredentialsId"
                echo "username: $USERNAME"
                sh "git clone --mirror https://$USERNAME:${PASSWORD.replace("@", "%40")}@bitbucket.org/surfstudio/android-standard/" //replace() for fix @ in repo url  

            }
        },
        pipeline.createStage("MIRRORING", StageStrategy.FAIL_WHEN_STAGE_ERROR) {
            dir("android-standard") {
                withCredentials([usernamePassword(credentialsId: mirrorRepoCredentialID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    echo "credentialsId: $mirrorRepoCredentialID"
                    echo "username: $USERNAME"
                    sh "git push --mirror https://$USERNAME:${PASSWORD.replace("@", "%40")}@github.com/surfstudio/SurfAndroidStandard.git"
                }
            }
        }
]

//run
pipeline.run()