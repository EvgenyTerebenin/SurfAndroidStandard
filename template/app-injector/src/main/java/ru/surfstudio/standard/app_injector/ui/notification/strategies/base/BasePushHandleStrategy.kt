package ru.surfstudio.standard.app_injector.ui.notification.strategies.base

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy
import ru.surfstudio.android.template.app_injector.R

/**
 * Базовая стратегия обработки push-уведомления
 */
abstract class BasePushHandleStrategy<out T : BaseNotificationTypeData<*>>
    : SimpleAbstractPushHandleStrategy<T>() {

    override val channelId: Int
        get() = R.string.notification_channel_id
    override val icon: Int
        get() = R.drawable.ic_launcher_background
    override val color: Int
        get() = R.color.colorPrimary

    override fun handlePushInActivity(activity: Activity): Boolean = false

    override fun preparePendingIntent(context: Context, title: String): PendingIntent {
        return PendingIntent.getActivity(
                context,
                title.hashCode(),
                coldStartRoute().prepareIntent(context),
                PendingIntent.FLAG_ONE_SHOT)
    }
}