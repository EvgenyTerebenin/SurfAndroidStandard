package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.base

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.firebase.sample.R
import ru.surfstudio.android.firebase.sample.ui.screen.push.PushActivityRoute
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy

/**
 * Базовый класс для пушей, основанных на [SimpleAbstractPushHandleStrategy],
 * определяющий их внешний вид и общие параметры
 */
abstract class BaseSimplePushStrategy<out T : BaseNotificationTypeData<*>>
    : SimpleAbstractPushHandleStrategy<T>() {

    override val channelId: Int
        get() = R.string.channel_id
    override val icon: Int
        get() = R.drawable.ic_android_black_24dp
    override val color: Int
        get() = R.color.colorPrimary

    override fun handlePushInActivity(activity: Activity): Boolean = false

    override fun coldStartRoute(): ActivityRoute = PushActivityRoute()

    override fun preparePendingIntent(context: Context, title: String): PendingIntent {
        return PendingIntent.getActivity(
                context,
                title.hashCode(),
                coldStartRoute().prepareIntent(context),
                PendingIntent.FLAG_ONE_SHOT)
    }
}