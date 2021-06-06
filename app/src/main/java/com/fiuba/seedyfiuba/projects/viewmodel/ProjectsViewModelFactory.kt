package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsUseCase
import com.fiuba.seedyfiuba.projects.usecases.SearchProjectsUseCase

/**
 * ViewModel provider factory to instantiate ProjectsViewModel.
 * Required given ProjectsViewModel has a non-empty constructor
 */
class ProjectsViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			GetProjectsUseCase::class.java,
			SearchProjectsUseCase::class.java
		).newInstance(
			ProjectsContainer.getProjectsUseCase,
			ProjectsContainer.searchProjectsUseCase
		)
	}
}
