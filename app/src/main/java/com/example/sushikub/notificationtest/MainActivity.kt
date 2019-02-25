package com.example.sushikub.notificationtest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.GROUP_ALERT_SUMMARY
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var stackBuilder: TaskStackBuilder
    private lateinit var list: List<Notifies>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stackBuilder = TaskStackBuilder.create(this)
        notificationManager = NotificationManagerCompat.from(this)
        list = NotificationDataFactory()

        start_notify.setOnClickListener {
            startNotify(list)
        }

        dismiss_notify.setOnClickListener {
            dismiss()
        }
    }

    private fun startNotify(data: List<Notifies>) {
        createParentNotification(data)
    }

    private fun createChildNotification(title: String, text: String): Notification {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
        stackBuilder.addNextIntent(intent)
        val pendingIntent = PendingIntent.getActivity(this, 0x128, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.abc_ic_star_black_16dp))
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_NAME)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addGroupAlertBehavior(builder)
        }

        return builder.build()
    }

    private fun createParentNotification(notifies: List<Notifies>) {

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notify_panel_notification_icon_bg))
                .setGroup(GROUP_NAME)
                .setAutoCancel(true)
                .setGroupSummary(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            addGroupAlertBehavior(builder)
        }

        notificationManager.apply {
            notifies.forEachIndexed { index, notifies ->
                notify(index + 1, createChildNotification(notifies.title, notifies.contentUrl))
            }
            notify(SUMMARY_ID, builder.build())
        }
    }

    private fun dismiss() {
        notificationManager.cancel(SUMMARY_ID)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(CHANNEL_ID, "links", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addGroupAlertBehavior(builder: NotificationCompat.Builder) {
        // Set alert only summary timing so that default setting makes users noisy
        builder.setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
    }

    companion object {
        private const val CHANNEL_ID = "USHI_ID"
        private const val GROUP_NAME = "USHI_GROUP"
        private const val SUMMARY_ID = 0
    }

}
