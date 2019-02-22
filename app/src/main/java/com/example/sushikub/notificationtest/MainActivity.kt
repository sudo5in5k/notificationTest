package com.example.sushikub.notificationtest

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val channelId = "USHI_ID"
    val group = "USHI_GROUP"
    private val summaryId = 0

    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var stackBuilder: TaskStackBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stackBuilder = TaskStackBuilder.create(this)
        notificationManager = NotificationManagerCompat.from(this)

        start_notify.setOnClickListener {
            startNotify()
        }

        dismiss_notify.setOnClickListener {
            dismiss()
        }
    }

    private fun startNotify() {
        val list = listOf(Notifies("amazon", "https://amazon.com"),
                Notifies("amazon", "https://amazon.com"),
                Notifies("amazon", "https://amazon.com"),
                Notifies("amazon", "https://amazon.com"))
        createParentNotification(list)
        Notification.InboxStyle()
    }

    private fun createChildNotification(title: String, text: String): Notification {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
        stackBuilder.addNextIntent(intent)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val noti = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.abc_ic_star_black_16dp))
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup(group)
                .build()
        Log.d(noti.number.toString(), "ushi")
        return noti
    }

    private fun createParentNotification(notifies: List<Notifies>) {

        val parent = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notify_panel_notification_icon_bg))
                .setGroup(group)
                .setAutoCancel(true)
                .setGroupSummary(true)
                .build()

        notificationManager.apply {
            notifies.forEachIndexed { index, notifies ->
                notify(index + 1, createChildNotification(notifies.title, notifies.contentUrl))
            }
            notify(summaryId, parent)
        }
    }

    private fun dismiss() {
        notificationManager.cancel(summaryId)
    }
}
