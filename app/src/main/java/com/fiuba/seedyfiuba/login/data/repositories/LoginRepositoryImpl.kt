package com.fiuba.seedyfiuba.login.data.repositories

import com.fiuba.seedyfiuba.login.domain.LoggedInUser
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.framework.requestmanager.datasources.LoginDataSourceImpl
import com.fiuba.seedyfiuba.login.view.activities.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryImpl(private val dataSource: LoginDataSourceImpl) : LoginRepository {

	// in-memory cache of the loggedInUser object
	var user: LoggedInUser? = null
		private set

	val isLoggedIn: Boolean
		get() = user != null

	init {
		// If user credentials will be cached in local storage, it is recommended it be encrypted
		// @see https://developer.android.com/training/articles/keystore
		user = null
	}

	override suspend fun logout() {
		user = null
		dataSource.logout()
	}

	override suspend fun login(username: String, password: String): Result<LoggedInUser> {
		// handle login
		val result = dataSource.login(username, password)

		if (result is Result.Success) {
			setLoggedInUser(result.data)
		}

		return result
	}

	override suspend fun register(username: String, password: String): Result<Session> {
		val result = dataSource.register(username, password)

		if (result is Result.Success) {
			setLoggedInUser(result.data.loggedInUser)
		}

		return result
	}


	private fun setLoggedInUser(loggedInUser: LoggedInUser) {
		this.user = loggedInUser
		// If user credentials will be cached in local storage, it is recommended it be encrypted
		// @see https://developer.android.com/training/articles/keystore
	}
}
