package com.fiuba.seedyfiuba.login.data.repositories

import com.fiuba.seedyfiuba.login.data.datasources.LoginLocalDataSource
import com.fiuba.seedyfiuba.login.data.datasources.LoginRemoteDataSource
import com.fiuba.seedyfiuba.login.domain.LoggedInUser
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.view.activities.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryImpl(
	private val remoteDataSource: LoginRemoteDataSource,
	private val localDataSource: LoginLocalDataSource

) : LoginRepository {

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
		remoteDataSource.logout()
	}

	override suspend fun login(username: String, password: String): Result<LoggedInUser> {
		// handle login
		val result = remoteDataSource.login(username, password)

		if (result is Result.Success) {
			//setLoggedInUser(result)
		}

		return result
	}

	override suspend fun register(
		username: String,
		password: String,
		profileType: String
	): Result<Session> {
		val result = remoteDataSource.register(username, password, profileType)

		if (result is Result.Success) {
			setLoggedInUser(result.data)
		}

		return result
	}

	override suspend fun saveSession(session: Session) {
		localDataSource.saveSession(session)
	}

	override suspend fun getSession(): Session {
		return localDataSource.getSession()
	}

	private suspend fun setLoggedInUser(session: Session) {
		saveSession(session)
		// If user credentials will be cached in local storage, it is recommended it be encrypted
		// @see https://developer.android.com/training/articles/keystore
	}
}
