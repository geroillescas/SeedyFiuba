package com.fiuba.seedyfiuba.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.fiuba.seedyfiuba.chat.model.*
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.data.repositories.ProfileRepository
import com.fiuba.seedyfiuba.profile.data.repositories.ProfileRepositoryImpl
import com.fiuba.seedyfiuba.commons.RequestManagerContainer
import com.fiuba.seedyfiuba.profile.requestmanager.datasources.ProfileRemoteDataSourceImpl
import com.fiuba.seedyfiuba.profile.usecases.*

@SuppressLint("StaticFieldLeak")
object ChatContainer {

	private lateinit var context: Context

	//Use Cases
	val sendNotificationUseCase: SendNotificationUseCase by lazy {
		SendNotificationUseCase(chatRepository)
	}

	private val chatRepository: ChatRepository by lazy {
		ChatRepositoryImpl(chatDataSource)
	}

	private val chatDataSource: ChatDataSource by lazy {
		ChatDataSourceImpl(RequestManagerContainer.chatApi, context)
	}

	fun init(context: Context) {
		ChatContainer.context = context
	}
}
