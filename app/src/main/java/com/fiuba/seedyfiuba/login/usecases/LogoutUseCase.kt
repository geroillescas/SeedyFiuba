package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository
import com.fiuba.seedyfiuba.login.domain.Session

class LogoutUseCase (private val loginRepository: LoginRepository) {
	suspend fun invoke() {
		loginRepository.logout()
	}
}
