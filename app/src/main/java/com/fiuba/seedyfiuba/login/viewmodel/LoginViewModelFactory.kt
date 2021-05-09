package com.fiuba.seedyfiuba.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.usecases.LoginUseCase

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			LoginUseCase::class.java
		).newInstance(
			LoginContainer.loginUseCase
		)
	}
}
