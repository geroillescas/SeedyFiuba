package com.fiuba.seedyfiuba.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.usecases.RegisterUseCase
import com.fiuba.seedyfiuba.login.usecases.SaveSessionUseCase

class RegisterViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			RegisterUseCase::class.java,
			SaveSessionUseCase::class.java
		).newInstance(
			LoginContainer.registerUseCase,
			LoginContainer.saveSessionUseCase
		)
	}
}
