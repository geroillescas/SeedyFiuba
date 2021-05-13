package com.fiuba.seedyfiuba.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.usecases.GetHolaMundoUseCase


/**
 * This factory should create the required viewModel by type for the view
 */
class HolaMundoViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(HolaMundoViewModel::class.java)) {
			return modelClass.getConstructor(
				GetHolaMundoUseCase::class.java
			).newInstance(
				LoginContainer.getHolaMundoUseCase
			)
		}
		throw IllegalStateException("ViewModel must extend ${modelClass.name}")
	}
}
