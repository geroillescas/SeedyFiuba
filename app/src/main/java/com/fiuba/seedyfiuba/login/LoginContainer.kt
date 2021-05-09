package com.fiuba.seedyfiuba.login

import android.content.Context
import com.fiuba.seedyfiuba.login.data.datasources.RemoteHolaMundoDataSource
import com.fiuba.seedyfiuba.login.data.repositories.HolaMundoRepository
import com.fiuba.seedyfiuba.login.data.repositories.HolaMundoRepositoryImpl
import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository
import com.fiuba.seedyfiuba.login.data.repositories.LoginRepositoryImpl
import com.fiuba.seedyfiuba.login.framework.requestmanager.RequestManagerContainer
import com.fiuba.seedyfiuba.login.framework.requestmanager.datasources.LoginDataSourceImpl
import com.fiuba.seedyfiuba.login.framework.requestmanager.datasources.RemoteHolaMundoDataSourceImpl
import com.fiuba.seedyfiuba.login.usecases.GetHolaMundoUseCase
import com.fiuba.seedyfiuba.login.usecases.LoginUseCase
import com.fiuba.seedyfiuba.login.usecases.RegisterUseCase

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


	private val holaMundoRepository: HolaMundoRepository by lazy {
		HolaMundoRepositoryImpl(
			remoteHolaMundoDataSource
		)
	}

	private val loginRepository: LoginRepository by lazy {
		LoginRepositoryImpl(
			loginDataSource
		)
	}

	private val loginDataSource: LoginDataSourceImpl by lazy {
		LoginDataSourceImpl(
			RequestManagerContainer.usersApi
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
