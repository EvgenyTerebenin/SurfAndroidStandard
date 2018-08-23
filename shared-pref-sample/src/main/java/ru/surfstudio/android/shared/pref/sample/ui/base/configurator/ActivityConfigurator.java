package ru.surfstudio.android.shared.pref.sample.ui.base.configurator;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;
import ru.surfstudio.android.shared.pref.sample.ui.base.dagger.activity.ActivityComponent;
import ru.surfstudio.android.shared.pref.sample.app.App;
import ru.surfstudio.android.shared.pref.sample.app.dagger.AppComponent;
import ru.surfstudio.android.shared.pref.sample.ui.base.dagger.activity.DaggerActivityComponent;

/**
 * Базовый конфигуратор для активити
 */

public class ActivityConfigurator extends BaseActivityConfigurator<ActivityComponent, AppComponent> {

    @Override
    protected ActivityComponent createActivityComponent(AppComponent parentComponent) {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected AppComponent getParentComponent() {
        return ((App) getTargetActivity().getApplicationContext()).getAppComponent();
    }
}
