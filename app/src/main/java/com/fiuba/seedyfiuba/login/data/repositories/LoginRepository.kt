package com.fiuba.seedyfiuba.login.data.repositories

import com.fiuba.seedyfiuba.login.domain.LoggedInUser
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.view.activities.Result

interface LoginRepository {
	suspend fun logout()
	suspend fun login(username: String, password: String): Result<LoggedInUser>
	suspend fun register(username: String, password: String, profileType: String): Result<Session>
	suspend fun saveSession(session: Session)
	suspend fun getSession(): Session
}
