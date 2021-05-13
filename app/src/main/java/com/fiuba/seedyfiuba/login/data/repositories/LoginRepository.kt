package com.fiuba.seedyfiuba.login.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.RegisterForm
import com.fiuba.seedyfiuba.login.domain.RegisteredInUser
import com.fiuba.seedyfiuba.login.domain.Session

interface LoginRepository {
	suspend fun logout()
	suspend fun login(email: String, password: String): Result<Session>
	suspend fun loginGoogle(token: String): Result<Session>
	suspend fun register(registerForm: RegisterForm): Result<RegisteredInUser>
	suspend fun saveSession(session: Session)
	suspend fun getSession(): Session
}
