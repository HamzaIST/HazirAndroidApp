package technology.innovate.haziremployee.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.utility.SessionManager


class FirebaseNotification : FirebaseMessagingService() {
    var notificationManager: NotificationManager? = null

    override fun onMessageReceived(message: RemoteMessage) {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        super.onMessageReceived(message)
        SessionManager.init(this)

        if (SessionManager.isNotification == true){
            notifyUser(
                message.data["title"].toString(),
                message.data["message"].toString()
            )
        }

        val intent = Intent("IntentFilterAction")
        intent.putExtra("MyDataKey",  message.data["title"].toString())
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    private fun notifyUser(title: String, messageBody: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
        var pendingIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        else {
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // For android Oreo and above  notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fcm notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}