package com.fiuba.seedyfiuba.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.fiuba.seedyfiuba.login.data.datasources.LoginLocalDataSource
import com.fiuba.seedyfiuba.login.data.datasources.LoginRemoteDataSource
import com.fiuba.seedyfiuba.login.data.datasources.RemoteHolaMundoDataSource
import com.fiuba.seedyfiuba.login.data.repositories.HolaMundoRepository
import com.fiuba.seedyfiuba.login.data.repositories.HolaMundoRepositoryImpl
import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository
import com.fiuba.seedyfiuba.login.data.repositories.LoginRepositoryImpl
import com.fiuba.seedyfiuba.login.framework.requestmanager.RequestManagerContainer
import com.fiuba.seedyfiuba.login.framework.requestmanager.datasources.LoginLocalDataSourceImpl
import com.fiuba.seedyfiuba.login.framework.requestmanager.datasources.LoginRemoteDataSourceImpl
import com.fiuba.seedyfiuba.login.framework.requestmanager.datasources.RemoteHolaMundoDataSourceImpl
import com.fiuba.seedyfiuba.login.usecases.*

object LoginContainer {
	private lateinit var context: Context

	//Use Cases
	val getHolaMundoUseCase: GetHolaMundoUseCase by lazy {
		GetHolaMundoUseCase(
			holaMundoRepository
		)
	}

	val registerUseCase: RegisterUseCase by lazy {
		RegisterUseCase(
			loginRepository
		)
	}

	val loginUseCase: LoginUseCase by lazy {
		LoginUseCase(
			loginRepository
		)
	}

	val getSessionUseCase: GetSessionUseCase by lazy {
		GetSessionUseCase(loginRepository)
	}

	val saveSessionUseCase: SaveSessionUseCase by lazy {
		SaveSessionUseCase(loginRepository)
	}

	private val holaMundoRepository: HolaMundoRepository by lazy {
		HolaMundoRepositoryImpl(
			remoteHolaMundoDataSource
		)
	}

	private val loginRepository: LoginRepository by lazy {
		LoginRepositoryImpl(
			loginRemoteDataSource,
			loginLocalDataSource
		)
	}

	private val loginLocalDataSource: LoginLocalDataSource by lazy {
		LoginLocalDataSourceImpl(
			sharedPreferences
		)
	}

	private val loginRemoteDataSource: LoginRemoteDataSource by lazy {
		LoginRemoteDataSourceImpl(
			RequestManagerContainer.usersApi
		)
	}

	private val remoteHolaMundoDataSource: RemoteHolaMundoDataSource by lazy {
		RemoteHolaMundoDataSourceImpl(
			RequestManagerContainer.holamundoApi
		)
	}

	private val sharedPreferences: SharedPreferences by lazy {
		context.getSharedPreferences("SEEDY_FIUB", MODE_PRIVATE)
	}

	fun init(context: Context) {
		LoginContainer.context = context
	}
}