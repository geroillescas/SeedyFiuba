package com.fiuba.seedyfiuba.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.usecases.RegisterUseCase

class RegisterViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			RegisterUseCase::class.java
		).newInstance(
			LoginContainer.registerUseCase
		)
	}
}
