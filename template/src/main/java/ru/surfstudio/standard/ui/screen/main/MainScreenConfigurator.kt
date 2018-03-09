package ru.surfstudio.standard.ui.screen.main

import android.content.Intent
import com.example.standarddialog.StandardDialogComponent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.standard.ui.base.dagger.screen.CustomScreenModule

/**
 * Конфигуратор активити главного экрана
 */
internal class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class], modules = [ActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent : ScreenComponent<MainActivityView>, StandardDialogComponent

    @Module
    internal class MainScreenModule(route: MainActivityRoute) :
            CustomScreenModule<MainActivityRoute>(route) {
    }

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
