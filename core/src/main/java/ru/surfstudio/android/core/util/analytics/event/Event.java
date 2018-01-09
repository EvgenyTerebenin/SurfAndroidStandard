package ru.surfstudio.android.core.util.analytics.event;

import java.util.Map;

/**
 * Описывает событие аналитики
 */
public interface Event {
    String key();
    Map<String, String> params();
}
