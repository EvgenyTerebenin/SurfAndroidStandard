/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin, Artem Zaytsev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.notification.ui.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import org.json.JSONArray
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Помощник создания нотификации в системном трее
 */
object NotificationCreateHelper {

    fun showNotification(
            context: Context,
            pushHandleStrategy: PushHandleStrategy<*>,
            pushId: Int,
            title: String,
            body: String
    ) {
        val notificationBuilder = pushHandleStrategy.notificationBuilder
                ?: buildNotification(pushHandleStrategy, title, body, context)

//        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

        /**
         * Only summary notification is implemented.
         * You can add all individual notification descriptions
         * as a line in the summary notification.
         * Since notification over riding won't work in this method,
         * we are saving notification description strings in an arraylist
         * and saving the arraylist object in sharedpref as a single string
         * by the help of Gson class.
         * when user clicks on appropirate notification, that sharedpref
         * key-value is cleared. Thus we can obtain the same behaviour
         * of stacked notification.
         **/

        /**
         * processStringDatas() method will return all the
         * saved strings in the SharedPreference which will
         * added as a line in the grouped notification.
         **/
        val groupId = pushHandleStrategy.group?.id
        if(groupId != null) {
            val notificationDescObject = NotificationGroupHelper
                    .processStringDatas(context, groupId, body)

            /**
             * Setting inboxStyle to notification to add each individual notification's
             * description.
             **/
            val inboxStyle = NotificationCompat.InboxStyle()

            notificationBuilder.setStyle(inboxStyle)

            inboxStyle.setBigContentTitle(title)

            /**
             * Magic happens here!
             * All the strings of our notificationDescObject is added
             * and grouping done.
             **/
            notificationDescObject.forEach { inboxStyle.addLine(it) }

            //Can set no.of messages as a summary.
            inboxStyle.setSummaryText(notificationDescObject.size.toString() + " Messages")

            getNotificationManager(context).notify(groupId, notificationBuilder.build())
            return
        }
//        } else {
//            SdkUtils.runOnOreo {
//                getNotificationManager(context).createNotificationChannel(
//                        pushHandleStrategy.channel
//                                ?: buildChannel(pushHandleStrategy, body, context)
//                )
//            }
//
////        val notificationBuilder = pushHandleStrategy.notificationBuilder
////                ?: buildNotification(pushHandleStrategy, title, body, context)
//
//            //создание заголовка группы нотификаций происходит вручную
//            pushHandleStrategy.group?.let {
//                getNotificationManager(context)
//                        .notify(it.id, pushHandleStrategy.groupSummaryNotificationBuilder?.build())
//            }
//        }
        getNotificationManager(context).notify(pushId, notificationBuilder.build())
    }

    @Deprecated("Используйте метод с pushId",
            ReplaceWith("showNotification(context, pushHandleStrategy, pushId, title, body"))
    fun showNotification(
            context: Context,
            pushHandleStrategy: PushHandleStrategy<*>,
            title: String,
            body: String
    ) {
        SdkUtils.runOnOreo {
            getNotificationManager(context).createNotificationChannel(
                    pushHandleStrategy.channel ?: buildChannel(pushHandleStrategy, body, context)
            )
        }

        val notificationBuilder = pushHandleStrategy.notificationBuilder
                ?: buildNotification(pushHandleStrategy, title, body, context)

        getNotificationManager(context).notify(title.hashCode(), notificationBuilder.build())
    }


    @SuppressLint("NewApi")
    private fun buildNotification(pushHandleStrategy: PushHandleStrategy<*>,
                                  title: String,
                                  body: String,
                                  context: Context): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, context.getString(pushHandleStrategy.channelId))
        return builder.setSmallIcon(pushHandleStrategy.icon)
                .setContentTitle(title)
                .setContentText(body)
                .setGroupSummary(true)
                .setColor(ContextCompat.getColor(context, pushHandleStrategy.color))
                .setContent(pushHandleStrategy.contentView)
                .setAutoCancel(pushHandleStrategy.autoCancelable)
                .setContentIntent(pushHandleStrategy.pendingIntent)
                .setDeleteIntent(pushHandleStrategy.deleteIntent ?: return builder)
    }


    @SuppressLint("NewApi")
    private fun buildChannel(pushHandleStrategy: PushHandleStrategy<*>,
                             body: String,
                             context: Context): NotificationChannel {
        val channel = NotificationChannel(
                context.getString(pushHandleStrategy.channelId),
                context.getString(pushHandleStrategy.channelName),
                NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = body
        channel.enableLights(true)
        channel.enableVibration(true)
        return channel
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}