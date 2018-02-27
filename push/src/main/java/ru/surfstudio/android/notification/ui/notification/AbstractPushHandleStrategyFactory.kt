package ru.surfstudio.android.notification.ui.notification

import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import java.util.*

/**
 * Фабрика стратегий обработки пуша по его типу
 */
abstract class AbstractPushHandleStrategyFactory {

    //ключ события в data firebase'вского пуша
    private val KEY = "event"

    /**
     * Переопределяем с необходимым соответствием действий(типа пуша) и стратегий
     */
    abstract val map: HashMap<String, PushHandleStrategy<*>>

    /**
     * Возвращает стратегию по данным пуша
     */
    fun createByData(data: Map<String, String>): PushHandleStrategy<*>? {
        Logger.d("data : ${data[KEY]}")
        return map[data[KEY]].apply {
            this?.typeData?.setDataFromMap(data)
        }
    }
}