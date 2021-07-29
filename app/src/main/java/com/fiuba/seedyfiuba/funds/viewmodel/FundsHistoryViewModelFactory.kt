package com.fiuba.seedyfiuba.funds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.funds.FundsContainer
import com.fiuba.seedyfiuba.funds.usecases.GetFundsUseCase
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.GetProjectUseCase

/**
 * This factory should create the required viewModel by type for the view
 */
class FundsHistoryViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(FundsHistoryViewModel::class.java)) {
			return modelClass.getConstructor(
				GetFundsUseCase::class.java,
				GetProjectUseCase::class.java
			).newInstance(
				FundsContainer.getFundsUseCase,
				ProjectsContainer.getProjectUseCase
			)
		}
		throw IllegalStateException("ViewModel must extend ${modelClass.name}")
	}
}

