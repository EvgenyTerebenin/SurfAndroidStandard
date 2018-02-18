package ru.surfstudio.standard.ui.base.configurator;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.standard.ui.base.dagger.ActivityComponent;
import ru.surfstudio.standard.ui.base.dagger.WidgetScreenModule;

/**
 * Базовый конфигуратор для WidgetView
 */

public abstract class WidgetScreenConfigurator
        extends BaseWidgetViewConfigurator<ActivityComponent, WidgetScreenModule> {

    @Override
    protected WidgetScreenModule getWidgetScreenModule() {
        return new WidgetScreenModule();
    }

    @Override
    protected ActivityComponent getParentComponent() {
        return (ActivityComponent) ((CoreActivityInterface) getTargetWidgetView().getContext())
                .getPersistentScope()
                .getConfigurator()
                .getActivityComponent();
    }
}
