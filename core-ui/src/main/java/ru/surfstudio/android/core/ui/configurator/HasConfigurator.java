package ru.surfstudio.android.core.ui.configurator;

/**
 * Интерфейс для экранов, имеющих конфигуратор
 */
public interface HasConfigurator {
    Configurator createConfigurator();
}
