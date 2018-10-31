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


import android.view.View;

import java.util.List;

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateEvent;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateEvent;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.event.WidgetLifecycleManager;
import ru.surfstudio.android.mvp.widget.event.delegate.WidgetScreenEventDelegateManager;
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

    private View widget;
    private CoreWidgetViewInterface coreWidgetView;
    private PersistentScopeStorage scopeStorage;
    private ParentPersistentScopeFinder parentPersistentScopeFinder;
    private String parentScopeId; // id родительского скоупа (необходим для получения уникального имени виджета)
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

  /*  public void initialize(@Nullable Bundle savedInstanceState) {
        currentScopeId = savedInstanceState != null
                ? savedInstanceState.getString(KEY_PSS_ID)
                : UUID.randomUUID().toString();
    }*/

    public void onCreate() {
        initPersistentScope();
        getScreenState().onCreate(widget, coreWidgetView);
        runConfigurator();
        coreWidgetView.bindPresenters();
        coreWidgetView.onCreate();
        Logger.d("11111 Widget sendEvent ViewReady");
        getEventDelegateManager().onCreate();

        getScreenState().onStart();
        Logger.d("11111 Widget sendEvent StartEvent");
        getEventDelegateManager().onStart();
//        getEventDelegateManager().sendEvent(new OnViewReadyEvent());
//        getEventDelegateManager().sendEvent(new OnStartEvent());
    }


    public void onResume() {
        Logger.d("11111 Widget sendEvent Resume");
        getScreenState().onResume();
        getEventDelegateManager().onResume();
//        getEventDelegateManager().sendEvent(new OnResumeEvent());
    }

    public void onPause() {
        Logger.d("11111 Widget sendEvent Pause");
        getScreenState().onPause();
        getEventDelegateManager().onPause();
//        getEventDelegateManager().sendEvent(new OnPauseEvent());
    }


    public void onDestroy() {
        Logger.d("11111 Widget sendEvent Stop");
        getScreenState().onStop();
        getEventDelegateManager().onStop();
//        getEventDelegateManager().sendEvent(new OnStopEvent());
        Logger.d("11111 Widget sendEvent ViewDestroy");
        getEventDelegateManager().onViewDetach();
//        getEventDelegateManager().sendEvent(new OnViewDestroyEvent());

        getScreenState().onDestroy();
        if (getScreenState().isCompletelyDestroyed()) {
            scopeStorage.remove(getScopeId());
        }
    }

    public WidgetViewPersistentScope getPersistentScope() {
        return scopeStorage.get(getScopeId(), WidgetViewPersistentScope.class);
    }

    private void runConfigurator() {
        getPersistentScope().getConfigurator().run();
    }

    private void initPersistentScope() {
        ScreenPersistentScope parentScope = parentPersistentScopeFinder.find();
        if (parentScope == null) {
            throw new IllegalStateException("WidgetView must be child of CoreActivityInterface or CoreFragmentInterface");
        }

        parentScopeId = parentScope.getScopeId();

        if (!scopeStorage.isExist(getScopeId())) {

            //todo отвзяать от скрин стейта экрана?
            WidgetScreenState screenState = new WidgetScreenState(parentScope.getScreenState());
            BaseWidgetViewConfigurator configurator = coreWidgetView.createConfigurator();
            ScreenEventDelegateManager parentDelegateManager = parentScope.getScreenEventDelegateManager();

            WidgetScreenEventDelegateManager delegateManager = new WidgetScreenEventDelegateManager(
                    eventResolvers,
                    parentDelegateManager
            );

            WidgetLifecycleManager lifecycleManager = new WidgetLifecycleManager(parentScope.getScreenState(), screenState, delegateManager);
            parentDelegateManager.register(lifecycleManager, OnRestoreStateEvent.class);
            parentDelegateManager.register(lifecycleManager, OnSaveStateEvent.class);
            parentDelegateManager.register(lifecycleManager, OnCompletelyDestroyEvent.class);

            WidgetViewPersistentScope persistentScope = new WidgetViewPersistentScope(
                    delegateManager,
                    screenState,
                    configurator,
                    getScopeId(),
                    lifecycleManager);
            configurator.setPersistentScope(persistentScope);
            scopeStorage.put(persistentScope);
        }
    }

    //getters

    private WidgetScreenState getScreenState() {
        return getPersistentScope().getScreenState();
    }

    private String getScopeId() {
        return coreWidgetView.getName() + parentScopeId;
    }

    private WidgetLifecycleManager getEventDelegateManager() {
        return getPersistentScope().getLifecycleManager();
    }

    public void onSaveInstanceState() {

    }
}
