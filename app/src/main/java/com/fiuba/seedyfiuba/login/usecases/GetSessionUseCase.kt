package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository

class GetSessionUseCase(private val loginRepository: LoginRepository) {
	suspend fun invoke() = loginRepository.getSession()
}
