package ru.surfstudio.android.notification.ui.notification

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.notification.interactor.push.PushInteractor

/**
 * Стратегия обработки пуша
 */
abstract class PushHandleStrategy<out T : BaseNotificationTypeData<*>> {

    companion object {
        const val EXTRA_PUSH_STRATEGY_KEY = "EXTRA_PUSH_STRATEGY"
    }

    /**
     * тип данных пуша [BaseNotificationTypeData]
     */
    abstract val typeData: T

    /**
     * Канал пуш уведомлений
     */
    abstract val channelId: String

    /**
     * Иконка
     */
    abstract val icon: Int

    /**
     * Флаг автозакрытия
     */
    abstract val autoCancelable: Boolean

    /**
     * Действия при нажатии на пуш
     */
    lateinit var pendingIntent: PendingIntent

    /**
     * Требуемое действие нотификации
     *
     * @param context      текущий контекст
     * @param pushInteractor пуш интерактор
     * @param title          заголвок пуша
     * @param body           сообщение пуша
     * @param data           данные пуша
     */
    fun handle(context: Context,
               pushInteractor: PushInteractor,
               title: String,
               body: String,
               data: Map<String, String>) {

        pendingIntent = preparePendingIntent(context, title)

        when (context) {
            is Activity -> if (handlePushInActivity(context)) {
                pushInteractor.onNewNotification(typeData)
            } else {
                showNotification(context, title, body)
            }

            else -> showNotification(context, title, body)
        }
    }

    private fun showNotification(context: Context, title: String, body: String) {
        NotificationCreateHelper.showNotification(context,
                this,
                title, body)
    }

    /**
     * Определяем в каких случайх пуш не надо отображать,
     * а необходимо выполнить те или иные действия по подписке
     */
    abstract fun handlePushInActivity(activity: Activity): Boolean

    /**
     * Интент в соответствии с необходимыми действиями
     */
    abstract fun preparePendingIntent(context: Context, title: String): PendingIntent

    /**
     * Интент при нажатии на пуш, если приложение в бэкграунде
     */
    abstract fun coldStartRoute(context: Context): ActivityRoute
}