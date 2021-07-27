package com.fiuba.seedyfiuba.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.funds.FundsContainer
import com.fiuba.seedyfiuba.funds.usecases.TransferUserCase
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.GetBalanceUseCase


/**
 * This factory should create the required viewModel by type for the view
 */
class HomeViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
			return modelClass.getConstructor(
				GetBalanceUseCase::class.java,
				TransferUserCase::class.java
			).newInstance(
				ProjectsContainer.getBalanceUseCase,
				FundsContainer.transferUserCase
			)
		}
		throw IllegalStateException("ViewModel must extend ${modelClass.name}")
	}
}
