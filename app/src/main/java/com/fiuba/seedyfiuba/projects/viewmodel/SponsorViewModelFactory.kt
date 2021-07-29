package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.GetBalanceUseCase
import com.fiuba.seedyfiuba.projects.usecases.SponsorUseCase

/**
 * ViewModel provider factory to instantiate ProjectsViewModel.
 * Required given ProjectsViewModel has a non-empty constructor
 */
class SponsorViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			SponsorUseCase::class.java,
			GetBalanceUseCase::class.java
		).newInstance(
			ProjectsContainer.sponsorUseCase,
			ProjectsContainer.getBalanceUseCase
		)
	}
}
