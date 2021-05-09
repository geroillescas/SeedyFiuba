package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository

class LoginUseCase(private val loginRepository: LoginRepository) {
	suspend fun invoke(username: String, password: String) =
		loginRepository.login(username, password)
}
