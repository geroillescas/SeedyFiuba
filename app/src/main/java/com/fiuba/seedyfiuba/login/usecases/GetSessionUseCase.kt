package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository
import com.fiuba.seedyfiuba.login.domain.Session

class GetSessionUseCase(private val loginRepository: LoginRepository) {
	suspend fun invoke(): Session {
		return loginRepository.getSession()
	}
}
