package ru.surfstudio.standard.app_injector.ui.notification.debug

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.support.v4.app.NotificationCompat
import ru.surfstudio.android.template.app_injector.BuildConfig
import ru.surfstudio.android.template.app_injector.R
import ru.surfstudio.android.utilktx.util.SdkUtils
import ru.surfstudio.standard.f_debug.debug.DebugActivityRoute

private val DEBUG_NOTIFICATION_ID = "DEBUG_NOTIFICATION_ID".hashCode()

object DebugNotificationBuilder {

    @SuppressLint("NewApi")
    fun showDebugNotification(context: Context) {
        if (canShowDebugScreen()) {
            val channelId = context.getString(R.string.notification_channel_id)
            val notificationTitle = context.getString(R.string.debug_screen_title)
            val notificationBody = context.getString(R.string.debug_screen_body)

            val pendingIntent = PendingIntent.getActivity(
                    context,
                    DEBUG_NOTIFICATION_ID,
                    DebugActivityRoute().prepareIntent(context),
                    PendingIntent.FLAG_ONE_SHOT)

            val notificationBuilder =  NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationBody)
                    .setContentIntent(pendingIntent)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (SdkUtils.isAtLeastOreo) {
                notificationManager.createNotificationChannel(
                        NotificationChannel(
                                channelId,
                                notificationTitle,
                                NotificationManager.IMPORTANCE_HIGH).apply {
                            description = notificationBody
                        }
                )
            }

            notificationManager.notify(DEBUG_NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun canShowDebugScreen(): Boolean {
        return BuildConfig.BUILD_TYPE == "debug" || BuildConfig.BUILD_TYPE == "qa"
    }
}