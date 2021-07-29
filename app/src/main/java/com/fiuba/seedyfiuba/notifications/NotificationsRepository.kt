package com.fiuba.seedyfiuba.notifications

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.gson.Gson

class NotificationsRepository(context: Context) {

	val prefs: SharedPreferences =
		context.getSharedPreferences("seedy_fiuba", FirebaseMessagingService.MODE_PRIVATE)
	val gson: Gson = Gson()

	fun saveNotification(seedyFiubaNotification: SeedyFiubaNotification){
		prefs.getString("notification_seedy_fiuba", null)?.let {

			val seedyFiubaNotificationList = gson.fromJson(it, Array<SeedyFiubaNotification>::class.java).toMutableList()
			if(!seedyFiubaNotificationList.contains(seedyFiubaNotification)) {
				seedyFiubaNotificationList.add(seedyFiubaNotification)
			}
			val serializedList = gson.toJson(seedyFiubaNotificationList)
			prefs.edit().apply {
				putString("notification_seedy_fiuba", serializedList)
				apply()
			}
            Log.d(TAG, "Saved new notification")
		} ?: run {
			val seedyFiubaNotificationList: Array<SeedyFiubaNotification> = arrayOf()

			val serializedList = gson.toJson(seedyFiubaNotificationList)
			prefs.edit().apply {
				putString("notification_seedy_fiuba", serializedList)
				apply()
			}
            Log.d(TAG, "Saved empty array notifications")
		}
	}

	fun removeNotification(seedyFiubaNotification: SeedyFiubaNotification){
		prefs.getString("notification_seedy_fiuba", null)?.let {
			val seedyFiubaNotificationList = gson.fromJson(it, Array<SeedyFiubaNotification>::class.java).toMutableList()
			if(seedyFiubaNotificationList.contains(seedyFiubaNotification)) {
				seedyFiubaNotificationList.remove(seedyFiubaNotification)
			}
			val serializedList = gson.toJson(seedyFiubaNotificationList)
			prefs.edit().apply {
				putString("notification_seedy_fiuba", serializedList)
				apply()
			}
            Log.d(TAG, "Removed notification")
		}
	}

	fun getNotifications(): List<SeedyFiubaNotification> {
		prefs.getString("notification_seedy_fiuba", null)?.let {
			Log.d(TAG, "Get all notifications")
			return gson.fromJson(it, Array<SeedyFiubaNotification>::class.java).toList()
		}?: run {
			return listOf()
		}
	}

	companion object {
		const val TAG = "NotificationsRepository"
	}
}
