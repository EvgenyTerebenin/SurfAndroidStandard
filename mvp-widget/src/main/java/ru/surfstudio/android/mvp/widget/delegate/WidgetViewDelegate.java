/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.mvp.widget.delegate;


import android.os.Parcelable;
import android.view.View;

import java.util.List;
import java.util.UUID;

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.event.WidgetLifecycleManager;
import ru.surfstudio.android.mvp.widget.event.delegate.WidgetScreenEventDelegateManager;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.state.WidgetParcelableState;
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

    private View widget;
    private CoreWidgetViewInterface coreWidgetView;
    private PersistentScopeStorage scopeStorage;
    private ParentPersistentScopeFinder parentPersistentScopeFinder;
    private List<ScreenEventResolver> eventResolvers;

    private String currentScopeId;

    public <W extends View & CoreWidgetViewInterface> WidgetViewDelegate(W coreWidgetView,
                                                                         PersistentScopeStorage scopeStorage,
                                                                         ParentPersistentScopeFinder parentPersistentScopeFinder,
                                                                         List<ScreenEventResolver> eventResolvers) {
        this.coreWidgetView = coreWidgetView;
        this.widget = coreWidgetView;
        this.scopeStorage = scopeStorage;
        this.parentPersistentScopeFinder = parentPersistentScopeFinder;
        this.eventResolvers = eventResolvers;
    }

    public void onCreate() {
        if (currentScopeId == null) {
            currentScopeId = UUID.randomUUID().toString();
        }

        initPersistentScope();
        getLifecycleManager().onCreate(widget, coreWidgetView, this);

        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();

        getLifecycleManager().onViewReady();
        getLifecycleManager().onStart();
    }

    public void onResume() {
        getLifecycleManager().onResume();
    }

    public void onPause() {
        getLifecycleManager().onPause();
    }

    public void onDestroy() {
        if (scopeStorage.isExist(getCurrentScopeId())) {
            getLifecycleManager().onStop();
            getLifecycleManager().onViewDestroy();
        }
    }

    //вызов происходит по срабатыванию родительского OnCompletelyDestroy
    public void onCompletelyDestroy() {
        if (getScreenState().isCompletelyDestroyed()) {
            scopeStorage.remove(getCurrentScopeId());
        }
    }

    public WidgetViewPersistentScope getPersistentScope() {
        return scopeStorage.get(getCurrentScopeId(), WidgetViewPersistentScope.class);
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        ScreenPersistentScope parentScope = parentPersistentScopeFinder.find();
        if (parentScope == null) {
            throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
        }

        if (!scopeStorage.isExist(getCurrentScopeId())) {

            WidgetScreenState screenState = new WidgetScreenState(parentScope.getScreenState());
            BaseWidgetViewConfigurator configurator = coreWidgetView.createConfigurator();
            ScreenEventDelegateManager parentDelegateManager = parentScope.getScreenEventDelegateManager();

            WidgetScreenEventDelegateManager delegateManager = new WidgetScreenEventDelegateManager(
                    eventResolvers,
                    parentDelegateManager
            );

            WidgetLifecycleManager lifecycleManager = new WidgetLifecycleManager(
                    parentScope.getScreenState(),
                    screenState,
                    delegateManager,
                    parentDelegateManager);

            WidgetViewPersistentScope persistentScope = new WidgetViewPersistentScope(
                    delegateManager,
                    screenState,
                    configurator,
                    getCurrentScopeId(),
                    lifecycleManager);

            configurator.setPersistentScope(persistentScope);
            scopeStorage.put(persistentScope);
        }
    }

    //getters

    private WidgetScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    private String getCurrentScopeId() {
        return currentScopeId;
    }

    private WidgetLifecycleManager getLifecycleManager() {
        return getPersistentScope().getLifecycleManager();
    }

    //пока оставил методы для восстановления
    public Parcelable onSaveInstanceState() {
        Parcelable superState = coreWidgetView.superSavedInstanceState();
        return new WidgetParcelableState(superState, currentScopeId);
    }

    public void onRestoreState(Parcelable state) {
        if (!(state instanceof WidgetParcelableState)) {
            coreWidgetView.superRestoreInstanceState(state);
            return;
        }

        WidgetParcelableState savedState = (WidgetParcelableState) state;
        currentScopeId = savedState.viewId;
        coreWidgetView.superRestoreInstanceState(savedState.getSuperState());
    }
}
