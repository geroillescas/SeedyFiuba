package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository

class LoginUseCase(private val loginRepository: LoginRepository) {
	suspend fun invoke(email: String, password: String) =
		loginRepository.login(email, password)

	suspend fun invoke(token: String) = loginRepository.loginGoogle(token)
}
