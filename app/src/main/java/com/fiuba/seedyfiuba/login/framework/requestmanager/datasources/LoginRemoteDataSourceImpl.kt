package com.fiuba.seedyfiuba.login.framework.requestmanager.datasources

import android.os.Parcelable
import com.fiuba.seedyfiuba.login.data.datasources.LoginRemoteDataSource
import com.fiuba.seedyfiuba.login.domain.RegisterForm
import com.fiuba.seedyfiuba.login.domain.RegisterResponseDTO
import com.fiuba.seedyfiuba.login.domain.RegisteredInUser
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.UsersApi
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.commons.SeedyFiubaError
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginRemoteDataSourceImpl(private val usersApi: UsersApi) : RemoteBaseDataSource(),
	LoginRemoteDataSource {

	override suspend fun login(email: String, password: String): Result<Session> {
		val loginRequestDTO = LoginRequestDTO(email, password)
		return getResult {
			usersApi.login(loginRequestDTO)
		}
	}

	override suspend fun logout() {
		// TODO: revoke authentication
	}

	override suspend fun loginGoogle(token: String): Result<Session> {
		val loginGoogleRequestDTO = LoginGoogleRequestDTO(token)
		return getResult {
			usersApi.loginGoogle(loginGoogleRequestDTO)
		}
	}

	override suspend fun register(
		registerForm: RegisterForm
	): Result<RegisterResponseDTO> {
		return getResult {
			usersApi.register(registerForm)
		}
	}
}

@Parcelize
data class LoginGoogleRequestDTO(
	@SerializedName("idToken")
	val idToken: String
) : Parcelable


data class LoginRequestDTO(
	val email: String,
	val password: String
)
