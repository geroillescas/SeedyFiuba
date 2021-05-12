package com.fiuba.seedyfiuba.login.data.datasources

import com.fiuba.seedyfiuba.login.domain.LoggedInUser
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.view.activities.Result

interface LoginRemoteDataSource {
	suspend fun login(username: String, password: String): Result<LoggedInUser>

	suspend fun logout()

	suspend fun register(username: String, password: String, profileType: String): Result<Session>
}
