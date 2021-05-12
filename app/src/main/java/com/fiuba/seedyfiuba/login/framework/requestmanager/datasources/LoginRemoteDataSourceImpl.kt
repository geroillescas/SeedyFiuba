package com.fiuba.seedyfiuba.login.framework.requestmanager.datasources

import com.fiuba.seedyfiuba.login.data.datasources.LoginRemoteDataSource
import com.fiuba.seedyfiuba.login.domain.LoggedInUser
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.UsersApi
import com.fiuba.seedyfiuba.login.view.activities.Result
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginRemoteDataSourceImpl(private val usersApi: UsersApi) : LoginRemoteDataSource {

	override suspend fun login(username: String, password: String): Result<LoggedInUser> {
		return try {
			// TODO: handle loggedInUser authentication
			val fakeUser = LoggedInUser(
				UUID.randomUUID().toString(), "Jane Doe", ""
			)
			Result.Success(
				fakeUser
			)
		} catch (e: Throwable) {
			Result.Error(
				IOException("Error logging in", e)
			)
		}
	}

	override suspend fun logout() {
		// TODO: revoke authentication
	}

	override suspend fun register(
		username: String,
		password: String,
		profileType: String
	): Result<Session> {
		return try {
			// TODO: handle loggedInUser authentication
			val fakeUser = LoggedInUser(
				UUID.randomUUID().toString(), "Jane Doe", ""
			)
			val session = Session(UUID.randomUUID().toString(), fakeUser, profileType)

			Result.Success(
				session
			)
		} catch (e: Throwable) {
			Result.Error(
				IOException("Error logging in", e)
			)
		}

	}
}
