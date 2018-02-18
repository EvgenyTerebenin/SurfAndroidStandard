package ru.surfstudio.android.mvp.widget.delegate;


import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * делегат для widget вью,
 * управляет ключевыми сущностями внутренней логики виджета:
 * - PersistentScope
 * - ScreenEventDelegateManager - соответствует менеджеру экрана-контейнера
 * - ScreenState - соответствует ScreenState экрана-контейнера
 * - ScreenConfigurator
 */
public class WidgetViewDelegate {

    private CoreWidgetViewInterface coreWidgetView;
    private PersistentScopeStorage scopeStorage;
    private ParentPersistentScopeFinder parentPersistentScopeFinder;

    public WidgetViewDelegate(CoreWidgetViewInterface coreWidgetView,
                              PersistentScopeStorage scopeStorage,
                              ParentPersistentScopeFinder parentPersistentScopeFinder) {
        this.coreWidgetView = coreWidgetView;
        this.scopeStorage = scopeStorage;
        this.parentPersistentScopeFinder = parentPersistentScopeFinder;
    }

    public void onCreate() {
        initPersistentScope();
        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();
    }

    public void onDestroy() {
        getScreenState().onDestroy();
        if (getScreenState().isCompletelyDestroyed()) {
            scopeStorage.remove(getName());
        }
    }

    protected WidgetViewPersistentScope getPersistentScope() {
        return scopeStorage.get(getName(), WidgetViewPersistentScope.class);
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        if (getPersistentScope() == null) {
            PersistentScope parentScope = parentPersistentScopeFinder.find();
            if (parentScope == null) {
                throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
            }
            WidgetScreenState screenState = new WidgetScreenState(parentScope.getScreenState());
            BaseWidgetViewConfigurator configurator = coreWidgetView.createConfigurator();
            WidgetViewPersistentScope persistentScope = new WidgetViewPersistentScope(
                    parentScope.getScreenEventDelegateManager(),
                    screenState,
                    configurator,
                    coreWidgetView.getName());
            configurator.setPersistentScope(persistentScope);
            scopeStorage.put(persistentScope);
        }
    }

    //getters

    private WidgetScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    private String getName() {
        return coreWidgetView.getName();
    }
}
