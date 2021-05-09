package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository

class RegisterUseCase(private val loginRepository: LoginRepository) {
	suspend fun invoke(username: String, password: String) =
		loginRepository.register(username, password)
}
