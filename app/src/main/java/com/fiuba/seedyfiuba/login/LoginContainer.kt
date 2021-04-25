package com.fiuba.seedyfiuba.login

import android.content.Context
import android.content.SharedPreferences
import com.fiuba.seedyfiuba.login.data.datasources.RemoteHolaMundoDataSource
import com.fiuba.seedyfiuba.login.data.repositories.HolaMundoRepository
import com.fiuba.seedyfiuba.login.data.repositories.HolaMundoRepositoryImpl
import com.fiuba.seedyfiuba.login.framework.requestmanager.RequestManagerContainer
import com.fiuba.seedyfiuba.login.framework.requestmanager.datasources.RemoteHolaMundoDataSourceImpl
import com.fiuba.seedyfiuba.login.usecases.GetHolaMundoUseCase

object LoginContainer {
	private lateinit var context: Context

	//Use Cases
	val getHolaMundoUseCase: GetHolaMundoUseCase by lazy {
		GetHolaMundoUseCase(
			holaMundoRepository
		)
	}

	private val holaMundoRepository: HolaMundoRepository by lazy {
		HolaMundoRepositoryImpl(
			remoteHolaMundoDataSource
		)
	}

	private val remoteHolaMundoDataSource: RemoteHolaMundoDataSource by lazy {
		RemoteHolaMundoDataSourceImpl(
			RequestManagerContainer.holamundoApi
		)
	}

	fun init(context: Context) {
		LoginContainer.context = context
	}
}
