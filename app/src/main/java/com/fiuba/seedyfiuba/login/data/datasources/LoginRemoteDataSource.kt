package com.fiuba.seedyfiuba.login.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.RegisterForm
import com.fiuba.seedyfiuba.login.domain.RegisterResponseDTO
import com.fiuba.seedyfiuba.login.domain.Session

interface LoginRemoteDataSource {
	suspend fun login(email: String, password: String): Result<Session>

	suspend fun logout()

	suspend fun loginGoogle(token: String): Result<Session>

	suspend fun register(registerForm: RegisterForm): Result<RegisterResponseDTO>
}
