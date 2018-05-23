package ru.surfstudio.android.rx.downloader.sample.ui.screen.main

import android.content.Intent

import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.downloader.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.rx.downloader.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.rx.downloader.sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.rx.downloader.sample.ui.base.dagger.screen.CustomScreenModule

class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(ActivityScreenModule::class, MainScreenModule::class))
    interface MainScreenComponent : ScreenComponent<MainActivityView> {
        //do nothing
    }

    @Module
    internal class MainScreenModule(route: MainActivityRoute) :
            CustomScreenModule<MainActivityRoute>(route)

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
