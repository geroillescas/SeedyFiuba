package com.fiuba.seedyfiuba.login.framework.requestmanager.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.data.datasources.LoginRemoteDataSource
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.RegisterResponseDTO
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.SessionDTO
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.UsersApi
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.LoginGoogleRequestDTO
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.LoginRequestDTO
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.RegisterFormDTO

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginRemoteDataSourceImpl(private val usersApi: UsersApi) : RemoteBaseDataSource(),
	LoginRemoteDataSource {

	override suspend fun login(email: String, password: String): Result<SessionDTO> {
		val loginRequestDTO =
			LoginRequestDTO(
				email,
				password
			)
		return getResult {
			usersApi.login(loginRequestDTO)
		}
	}

	override suspend fun logout() {
		// TODO: revoke authentication
	}

	override suspend fun loginGoogle(token: String): Result<SessionDTO> {
		val loginGoogleRequestDTO =
			LoginGoogleRequestDTO(
				token
			)
		return getResult {
			usersApi.loginGoogle(loginGoogleRequestDTO)
		}
	}

	override suspend fun register(
		registerForm: RegisterFormDTO
	): Result<RegisterResponseDTO> {
		return getResult {
			usersApi.register(registerForm)
		}
	}
}
