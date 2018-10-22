package ru.surfstudio.android.firebase.sample.ui.base.configurator;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.firebase.sample.app.AppConfigurator;
import ru.surfstudio.android.firebase.sample.app.CustomApp;
import ru.surfstudio.android.firebase.sample.app.dagger.CustomAppComponent;
import ru.surfstudio.android.firebase.sample.ui.base.dagger.activity.CustomActivityComponent;
import ru.surfstudio.android.firebase.sample.ui.base.dagger.activity.DaggerCustomActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule;

/**
 * Базовый конфигуратор для активити
 */
public class CustomActivityConfigurator extends BaseActivityConfigurator<CustomActivityComponent, CustomAppComponent> {

    @Override
    protected CustomActivityComponent createActivityComponent(CustomAppComponent parentComponent) {
        return DaggerCustomActivityComponent.builder()
                .customAppComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected CustomAppComponent getParentComponent() {
        return AppConfigurator.INSTANCE.getCustomAppComponent();
    }
}
