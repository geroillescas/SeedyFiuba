package com.fiuba.seedyfiuba.notifications

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.MainActivity
import com.fiuba.seedyfiuba.R

class CenterNotificationActivity : BaseActivity(),
	CenterNotificationRecyclerViewAdapter.NotificationRecyclerViewAdapterListener {

	lateinit var centerNotificationList: RecyclerView
	override var layoutResource: Int = R.layout.activity_center
	lateinit var notificationsRepository: NotificationsRepository

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		notificationsRepository = NotificationsRepository(this)

		centerNotificationList = findViewById(R.id.activityCenterNotification_list)
		val adapter = CenterNotificationRecyclerViewAdapter(mutableListOf(), this)
		centerNotificationList.adapter = adapter

		centerNotificationList.layoutManager = LinearLayoutManager(this)

		adapter.setNotifications(notificationsRepository.getNotifications())
	}

	override fun onNotificationClicked(
		seedyFiubaNotification: SeedyFiubaNotification,
		position: Int
	) {
		val intent = Intent(this, MainActivity::class.java)
		intent.putExtra(PushHandleActivity.NOTIFICACION, seedyFiubaNotification)
		notificationsRepository.removeNotification(seedyFiubaNotification)
		startActivity(intent)
	}
}

