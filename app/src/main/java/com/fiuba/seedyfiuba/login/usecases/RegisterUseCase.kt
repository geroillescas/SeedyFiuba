package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.LoginRepository
import com.fiuba.seedyfiuba.login.domain.RegisterForm

class RegisterUseCase(private val loginRepository: LoginRepository) {
	suspend fun invoke(registerForm: RegisterForm) = loginRepository.register(registerForm)
}
