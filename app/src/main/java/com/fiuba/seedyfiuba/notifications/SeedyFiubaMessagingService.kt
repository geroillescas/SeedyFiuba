package com.fiuba.seedyfiuba.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fiuba.seedyfiuba.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SeedyFiubaMessagingService : FirebaseMessagingService() {

	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		// ...

		// TODO(developer): Handle FCM messages here.
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		Log.d(TAG, "From: ${remoteMessage.from}")

		// Check if message contains a data payload.
		if (remoteMessage.data.isNotEmpty()) {
			Log.d(TAG, "Message data payload: ${remoteMessage.data}")

		}

		// Check if message contains a notification payload.
		remoteMessage.notification?.let {
			Log.d(TAG, "Message Notification Body: ${it.body}")
			val seedyFiubaNotification = SeedyFiubaNotification(
				title = it.title,
				body = it.body,
				imageUrl = it.imageUrl,
				extra = remoteMessage.data
			)
			sendNotification(seedyFiubaNotification)
		}

		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.
	}

	override fun onNewToken(token: String) {
		Log.d(TAG, "Refreshed token: $token")

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// FCM registration token to your app server.
		//sendRegistrationToServer(token)
	}

	private fun sendNotification(seedyFiubaNotification: SeedyFiubaNotification) {
		val intent = Intent(this, PushHandleActivity::class.java)
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		intent.putExtra(PushHandleActivity.NOTIFICACION, seedyFiubaNotification)
		val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
			PendingIntent.FLAG_ONE_SHOT)


		val channelId = getString(R.string.default_notification_channel_id)
		val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
		val notificationBuilder = NotificationCompat.Builder(this, channelId)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle(getString(R.string.fcm_message))
			.setContentText(seedyFiubaNotification.body)
			.setAutoCancel(true)
			.setSound(defaultSoundUri)
			.setContentIntent(pendingIntent)

		val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(channelId,
				"Channel human readable title",
				NotificationManager.IMPORTANCE_DEFAULT)
			notificationManager.createNotificationChannel(channel)
		}

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
	}

	companion object {
		const val TAG = "SeedyFiubaMessaging"
	}
}
