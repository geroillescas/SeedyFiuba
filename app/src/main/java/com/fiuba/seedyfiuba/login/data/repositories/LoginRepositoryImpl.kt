package com.fiuba.seedyfiuba.login.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.data.datasources.LoginLocalDataSource
import com.fiuba.seedyfiuba.login.data.datasources.LoginRemoteDataSource
import com.fiuba.seedyfiuba.login.domain.*
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.RegisterFormDTO

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryImpl(
	private val remoteDataSource: LoginRemoteDataSource,
	private val localDataSource: LoginLocalDataSource
) : LoginRepository {

	// in-memory cache of the loggedInUser object
	var user: RegisteredInUser? = null
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
		localDataSource.logout()
	}

	override suspend fun login(email: String, password: String): Result<Session> {
		// handle login
		return remoteDataSource.login(email, password).let {
			when (it) {
				is Result.Success -> {
					val user = with(it.data.user) {
						User(
							userId ?: "",
							name,
							lastName ?: "",
							email,
							password,
							profileType ?: ProfileType.UNKNOWN
						)
					}
					val session = Session(it.data.token, user)
					saveSession(session)
					Result.Success(session)
				}
				is Result.Error -> it
			}
		}
	}

	override suspend fun loginGoogle(token: String): Result<Session> {
		return remoteDataSource.loginGoogle(token).let {
			when (it) {
				is Result.Success -> {
					val user = with(it.data.user) {
						User(
							userId ?: "",
							name,
							lastName ?: "",
							email ?: "",
							password ?: "",
							profileType ?: ProfileType.UNKNOWN
						)
					}
					val session = Session(it.data.token, user)
					saveSession(session)
					Result.Success(session)
				}
				is Result.Error -> it
			}
		}
	}

	override suspend fun register(
		registerForm: RegisterForm
	): Result<RegisteredInUser> {
			val registerFormDTO =
			RegisterFormDTO(
				registerForm.name,
				registerForm.lastName,
				registerForm.email,
				registerForm.password,
				registerForm.profileType.value
			)
		return when (val result = remoteDataSource.register(registerFormDTO)) {
			is Result.Success -> Result.Success(RegisteredInUser(result.data.userId))
			is Result.Error -> result
		}
	}

	override suspend fun saveSession(session: Session) {
		localDataSource.saveSession(session)
	}

	override suspend fun getSession(): Session? {
		return localDataSource.getSession()
	}

	private suspend fun setLoggedInUser(session: Session) {
		saveSession(session)
		// If user credentials will be cached in local storage, it is recommended it be encrypted
		// @see https://developer.android.com/training/articles/keystore
	}
}
