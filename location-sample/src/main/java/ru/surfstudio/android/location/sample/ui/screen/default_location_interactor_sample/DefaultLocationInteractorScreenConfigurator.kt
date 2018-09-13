package ru.surfstudio.android.location.sample.ui.screen.default_location_interactor_sample

import android.content.Context
import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.location.DefaultLocationInteractor
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

/**
 * Конфигуратор экрана [DefaultLocationInteractorActivityView]
 */
class DefaultLocationInteractorScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, DefaultLocationInteractorScreenModule::class])
    internal interface DefaultLocationInteractorScreenComponent :
            ScreenComponent<DefaultLocationInteractorActivityView>

    @Module
    internal class DefaultLocationInteractorScreenModule {

        @Provides
        fun provideLocationService(context: Context) = LocationService(context)

        @Provides
        fun provideDefaultLocationInteractor(
                permissionManager: PermissionManager,
                screenEventDelegateManager: ScreenEventDelegateManager,
                activityProvider: ActivityProvider,
                locationService: LocationService
        ) = DefaultLocationInteractor(permissionManager, screenEventDelegateManager, activityProvider, locationService)

        @Provides
        fun provideDefaultLocationInteractorPresenter(
                screenEventDelegateManager: ScreenEventDelegateManager,
                screenState: ScreenState,
                schedulersProvider: SchedulersProvider,
                defaultLocationInteractor: DefaultLocationInteractor
        ) = DefaultLocationInteractorPresenter(
                screenEventDelegateManager,
                screenState,
                schedulersProvider,
                defaultLocationInteractor
        )
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDefaultLocationInteractorScreenConfigurator_DefaultLocationInteractorScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .defaultLocationInteractorScreenModule(DefaultLocationInteractorScreenModule())
                .build()
    }
}