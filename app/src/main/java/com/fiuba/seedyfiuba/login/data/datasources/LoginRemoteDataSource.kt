package com.fiuba.seedyfiuba.login.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.RegisterFormDTO
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.RegisterResponseDTO
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.SessionDTO

interface LoginRemoteDataSource {
	suspend fun login(email: String, password: String): Result<SessionDTO>

	suspend fun logout()

	suspend fun loginGoogle(token: String): Result<SessionDTO>

	suspend fun register(registerForm: RegisterFormDTO): Result<RegisterResponseDTO>
}
