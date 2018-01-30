package ru.surfstudio.android.core.ui.base.navigation.activity.navigator;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.event.delegate.activity.result.BaseActivityResultDelegate;
import ru.surfstudio.android.core.ui.base.event.delegate.newintent.NewIntentDelegate;
import ru.surfstudio.android.core.ui.base.navigation.Navigator;
import ru.surfstudio.android.core.ui.base.navigation.ScreenResult;
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityRoute;
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityWithResultRoute;
import ru.surfstudio.android.core.ui.base.navigation.activity.route.NewIntentRoute;

/**
 * позволяет осуществлять навигацияю между активити
 */
public abstract class ActivityNavigator extends BaseActivityResultDelegate //todo unique start from screen name?
        implements Navigator, NewIntentDelegate {

    private Map<NewIntentRoute, Subject> newIntentSubjects = new HashMap<>();
    private final ActivityProvider activityProvider;


    public ActivityNavigator(ActivityProvider activityProvider,
                             ScreenEventDelegateManager eventDelegateManager) {
        eventDelegateManager.registerDelegate(this);
        this.activityProvider = activityProvider;
    }

    protected abstract void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle bundle);

    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            Class<? extends ActivityWithResultRoute<T>> routeClass) {
        try {
            return this.observeOnActivityResult(routeClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("route class " + routeClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }

    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            ActivityWithResultRoute route) {
        return super.observeOnActivityResult(route);
    }

    public void finishCurrent() {
        activityProvider.get().finish();
    }

    public void finishAffinity(){
        ActivityCompat.finishAffinity(activityProvider.get());
    }

    public <T extends Serializable> void finishWithResult(ActivityWithResultRoute<T> activeScreenRoute,
                                                          boolean success) {
        finishWithResult(activeScreenRoute, null, success);
    }

    public <T extends Serializable> void finishWithResult(ActivityWithResultRoute<T> activeScreenRoute,
                                                          T result) {
        finishWithResult(activeScreenRoute, result, true);
    }

    public <T extends Serializable> void finishWithResult(ActivityWithResultRoute<T> currentScreenRoute,
                                                          T result, boolean success) {
        Intent resultIntent = currentScreenRoute.prepareResultIntent(result);
        activityProvider.get().setResult(
                success ? Activity.RESULT_OK : Activity.RESULT_CANCELED,
                resultIntent);
        finishCurrent();
    }

    /**
     * Запуск активити.
     *
     * @param route роутер
     * @return {@code true} если активити успешно запущен, иначе {@code false}
     */
    public boolean start(ActivityRoute route) {
        Context context = activityProvider.get();
        Intent intent = route.prepareIntent(context);
        Bundle bundle = route.prepareBundle();
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent, bundle);
            return true;
        }

        return false;
    }

    /**
     * Запуск активити.
     *
     * @param route роутер
     * @return {@code true} если активити успешно запущен, иначе {@code false}
     */
    public boolean startForResult(ActivityWithResultRoute route) {
        if (!super.isObserved(route)) {
            throw new IllegalStateException("route class " + route.getClass().getSimpleName()
                    + " must be registered by method ActivityNavigator#observeResult");
        }

        Context context = activityProvider.get();
        Intent intent = route.prepareIntent(context);
        Bundle bundle = route.prepareBundle();
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, route.getRequestCode(), bundle);
            return true;
        }

        return false;
    }

    // =========================  NEW INTENT =================================

    @Override
    public boolean onNewIntent(Intent intent) {
        for (NewIntentRoute route : newIntentSubjects.keySet()) {
            if (route.parseIntent(intent)) {
                Subject resultSubject = newIntentSubjects.get(route);
                resultSubject.onNext(route);
                return true;
            }
        }
        return false;
    }

    public <T extends NewIntentRoute> Observable<T> observeNewIntent(Class<T> newIntentRouteClass) {
        try {
            return this.observeNewIntent(newIntentRouteClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("route class " + newIntentRouteClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }

    public <T extends NewIntentRoute> Observable<T> observeNewIntent(T newIntentRoute) {
        tryRemoveDuplicateEventSubjects(newIntentRoute);
        PublishSubject<T> eventSubject = PublishSubject.create();
        newIntentSubjects.put(newIntentRoute, eventSubject);
        return eventSubject;
    }

    private void tryRemoveDuplicateEventSubjects(NewIntentRoute eventParser) {
        for (NewIntentRoute registeredRoute : newIntentSubjects.keySet()) {
            if (registeredRoute.getClass().getCanonicalName().equals(eventParser.getClass().getCanonicalName())) {
                newIntentSubjects.get(registeredRoute).onComplete();
                newIntentSubjects.remove(registeredRoute);
                Log.v(this.getClass().getName(), "duplicate registered NewIntentRoute :"
                        + registeredRoute + " old route unregistered");
            }
        }
    }
}