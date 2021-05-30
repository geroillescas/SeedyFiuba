package com.fiuba.seedyfiuba.login.framework.requestmanager.api

import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApi {
	/**
	 * Requests authorization to see if user has enough balance.
	 *
	 * @return A object containing whether the user has enough balance in a [BalanceDto]
	 */
	@POST(LoginConstant.END_POINT_REGISTER)
	suspend fun register(
		@Body registerForm: RegisterFormDTO
	): Response<RegisterResponseDTO>

	@POST(LoginConstant.END_POINT_LOGIN)
	suspend fun login(
		@Body loginRequestDTO: LoginRequestDTO
	): Response<SessionDTO>

	@POST(LoginConstant.END_POINT_GOOGLE_LOGIN)
	suspend fun loginGoogle(
		@Body loginGoogleRequestDTO: LoginGoogleRequestDTO
	): Response<SessionDTO>
}
