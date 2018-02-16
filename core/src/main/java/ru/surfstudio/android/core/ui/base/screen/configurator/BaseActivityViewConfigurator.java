package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.app.Activity;
import android.content.Intent;

import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityViewPersistentScope;

/**
 * Базовый класс конфигуратора экрана, основанного на Activity, см {@link ViewConfigurator}
 *
 * @param <P> родительский корневой даггер компонент
 * @param <M> даггер модуль для активити
 * @param <A> родительский ActivityComponent
 */
public abstract class BaseActivityViewConfigurator<P, A, M>
        extends BaseActivityConfigurator<A, P>
        implements ViewConfigurator {

    private Intent intent;
    private ScreenComponent screenComponent;
    private ActivityViewPersistentScope persistentScope;


    public BaseActivityViewConfigurator(Intent intent) {
        this.intent = intent;
    }

    protected abstract ScreenComponent createScreenComponent(A parentActivityComponent,
                                                             M activityScreenModule,
                                                             CoreActivityScreenModule coreActivityScreenModule,
                                                             Intent intent);

    protected abstract M getActivityScreenModule();

    @Override
    public void run() {
        super.run();
        satisfyDependencies(getTargetActivityView());
    }

    protected <T extends Activity & CoreActivityViewInterface> T getTargetActivityView() {
        return (T)getPersistentScope().getScreenState().getCoreActivityView();
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return screenComponent;
    }

    private void satisfyDependencies(CoreActivityViewInterface target) {
        if (screenComponent == null) {
            screenComponent = createScreenComponent();
        }
        screenComponent.inject(target);
    }

    protected Intent getIntent() {
        return intent;
    }

    private ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getActivityComponent(),
                getActivityScreenModule(),
                new CoreActivityScreenModule(),
                getIntent());
    }

    @Override
    protected ActivityViewPersistentScope getPersistentScope() {
        return persistentScope;
    }

    public void setPersistentScope(ActivityViewPersistentScope persistentScope) {
        super.setPersistentScope(persistentScope);
        this.persistentScope = persistentScope;
    }

    @Override
    @Deprecated
    public void setPersistentScope(ActivityPersistentScope persistentScreenScope) {
        throw new UnsupportedOperationException("call another setPersistentScope");
    }
}
