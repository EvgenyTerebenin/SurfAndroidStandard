package ru.surfstudio.standard.ui.base.dagger

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.message.DefaultMessageController
import ru.surfstudio.android.core.ui.message.MessageController
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.permission.PermissionManagerForActivity
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.dagger.DialogNavigatorForActivityModule
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = arrayOf(
        ErrorHandlerModule::class,
        DialogNavigatorForActivityModule::class))
class ActivityScreenModule {
    @Provides
    @PerScreen
    internal fun providePersistentScope(persistentScope: ActivityPersistentScope): PersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    internal fun provideScreenState(persistentScope: ActivityPersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(activityProvider: ActivityProvider,
                                          eventDelegateManager: ScreenEventDelegateManager): ActivityNavigator {
        return ActivityNavigatorForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator {
        return FragmentNavigator(activityProvider)
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManagerProvider(persistentScope: PersistentScope): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerScreen
    internal fun providePermissionManager(activityProvider: ActivityProvider,
                                          eventDelegateManager: ScreenEventDelegateManager): PermissionManager {
        return PermissionManagerForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(activityProvider: ActivityProvider): MessageController {
        return DefaultMessageController(activityProvider)
    }

    @PerScreen
    @Provides
    internal fun provideBaseDependency(schedulersProvider: SchedulersProvider,
                                       screenState: ScreenState,
                                       eventDelegateManager: ScreenEventDelegateManager,
                                       connectionProvider: ConnectionProvider,
                                       activityNavigator: ActivityNavigator): BasePresenterDependency {
        return BasePresenterDependency(schedulersProvider,
                screenState,
                eventDelegateManager,
                connectionProvider,
                activityNavigator)
    }
}