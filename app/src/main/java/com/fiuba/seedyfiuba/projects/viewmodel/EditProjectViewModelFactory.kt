package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.SaveProjectUseCase
import com.fiuba.seedyfiuba.projects.usecases.UpdateProjectUseCase

/**
 * ViewModel provider factory to instantiate ProjectsViewModel.
 * Required given ProjectsViewModel has a non-empty constructor
 */
class EditProjectViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			SaveProjectUseCase::class.java,
			UpdateProjectUseCase::class.java
		).newInstance(
			ProjectsContainer.saveProjectUseCase,
			ProjectsContainer.updateProjectUseCase
		)
	}
}
