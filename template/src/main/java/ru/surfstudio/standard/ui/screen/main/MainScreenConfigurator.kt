package ru.surfstudio.standard.ui.screen.main

import android.app.Activity
import android.content.Intent

import dagger.Component
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenComponent
import ru.surfstudio.standard.app.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule

/**
 * Конфигуратор активити главного экрана
 */
internal class MainScreenConfigurator(activity: Activity, intent: Intent) : ActivityScreenConfigurator(activity, intent) {
    @PerScreen
    @Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(ActivityScreenModule::class))
    internal interface MainScreenComponent : ScreenComponent<MainActivityView>

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       coreActivityScreenModule: CoreActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(activityComponent)
                .coreActivityScreenModule(coreActivityScreenModule)
                .activityScreenModule(activityScreenModule)
                .build()
    }

    override fun getName(): String {
        return "main"
    }
}
