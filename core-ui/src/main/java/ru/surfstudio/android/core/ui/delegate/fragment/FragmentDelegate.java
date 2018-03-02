package ru.surfstudio.android.core.ui.delegate.fragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.delegate.base.BaseScreenDelegate;
import ru.surfstudio.android.core.ui.event.FragmentScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.state.FragmentScreenState;

/**
 * делегат для базового фрагмента,
 * управляет ключевыми сущностями внутренней логики экрана:
 * - PersistentScope            - хранилище для всех остальных обьектов, переживает смену конфигурации
 * - ScreenEventDelegateManager - позволяет подписываться на события экрана
 * - ScreenState                - хранит текущее состояние экрана
 * - ScreenConfigurator         - управляет компонентами даггера, предоставляет уникальное имя экрана
 */
public class FragmentDelegate extends BaseScreenDelegate {

    private Fragment fragment;
    private CoreFragmentInterface coreFragment;
    private PersistentScopeStorage scopeStorage;

    public <F extends Fragment & CoreFragmentInterface> FragmentDelegate(
            F fragment,
            PersistentScopeStorage scopeStorage,
            List<ScreenEventResolver> eventResolvers,
            FragmentCompletelyDestroyChecker completelyDestroyChecker) {
        super(scopeStorage, eventResolvers, completelyDestroyChecker);
        this.fragment = fragment;
        this.coreFragment = fragment;
        this.scopeStorage = scopeStorage;
    }

    @Override
    public FragmentPersistentScope getPersistentScope() {
        return scopeStorage.get(getScopeId(), FragmentPersistentScope.class);
    }

    @Override
    public FragmentScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    @Override
    protected void notifyScreenStateAboutOnCreate(@Nullable Bundle savedInstanceState) {
        this.getScreenState().onCreate(fragment, coreFragment, savedInstanceState);
    }

    @Override
    protected void prepareView(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle ignore) {
        coreFragment.onActivityCreated(savedInstanceState, getScreenState().isViewRecreated());
    }

    @NonNull
    @Override
    protected FragmentPersistentScope createPersistentScope(List<ScreenEventResolver> eventResolvers) {
        FragmentScreenEventDelegateManager eventDelegateManager = createFragmentScreenEventDelegateManager(eventResolvers);
        FragmentScreenState screenState = new FragmentScreenState();
        BaseFragmentConfigurator configurator = coreFragment.createConfigurator();
        FragmentPersistentScope persistentScope = new FragmentPersistentScope(
                eventDelegateManager,
                screenState,
                configurator,
                getScopeId());
        return persistentScope;
    }

    @NonNull
    protected FragmentScreenEventDelegateManager createFragmentScreenEventDelegateManager(List<ScreenEventResolver> eventResolvers) {
        ActivityPersistentScope activityPersistentScope = scopeStorage.getActivityScope();
        if (activityPersistentScope == null) {
            throw new IllegalStateException("FragmentPersistentScope cannot be created without ActivityPersistentScope");
        }
        return new FragmentScreenEventDelegateManager(
                eventResolvers,
                activityPersistentScope.getScreenEventDelegateManager());
    }
}
