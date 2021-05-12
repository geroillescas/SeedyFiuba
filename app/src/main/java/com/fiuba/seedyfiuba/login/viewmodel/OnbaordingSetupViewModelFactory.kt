package com.fiuba.seedyfiuba.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.usecases.GetSessionUseCase

class OnbaordingSetupViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			GetSessionUseCase::class.java
		).newInstance(
			LoginContainer.getSessionUseCase
		)
	}
}
