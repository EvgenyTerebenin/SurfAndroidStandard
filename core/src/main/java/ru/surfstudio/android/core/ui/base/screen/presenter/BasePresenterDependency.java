package ru.surfstudio.android.core.ui.base.screen.presenter;


import javax.inject.Inject;

import ru.surfstudio.android.core.app.connection.ConnectionProvider;
import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;
import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;

/**
 * зависимости базового презентера
 * Обернуты в отдельный обьект чтобы не занимать много места в конструкторах классов наследников {@link BasePresenter}
 */
@PerScreen
public class BasePresenterDependency {

    private SchedulersProvider schedulersProvider;
    private ScreenState screenState;
    private ScreenEventDelegateManager eventDelegateManager;

    private ActivityNavigator activityNavigator;
    private ConnectionProvider connectionProvider;

    @Inject
    public BasePresenterDependency(SchedulersProvider schedulersProvider,
                                   ScreenState screenState,
                                   ScreenEventDelegateManager eventDelegateManager,
                                   ConnectionProvider connectionProvider,
                                   ActivityNavigator activityNavigator) {
        this.schedulersProvider = schedulersProvider;
        this.screenState = screenState;
        this.eventDelegateManager = eventDelegateManager;
        this.connectionProvider = connectionProvider;
        this.activityNavigator = activityNavigator;
    }

    public SchedulersProvider getSchedulersProvider() {
        return schedulersProvider;
    }

    public ScreenEventDelegateManager getEventDelegateManager() {
        return eventDelegateManager;
    }

    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public ScreenState getScreenState() {
        return screenState;
    }
}
