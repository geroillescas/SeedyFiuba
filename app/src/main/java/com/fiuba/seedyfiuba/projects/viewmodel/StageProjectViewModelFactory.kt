package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.SaveProjectUseCase

/**
 * ViewModel provider factory to instantiate ProjectsViewModel.
 * Required given ProjectsViewModel has a non-empty constructor
 */
class StageProjectViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			SaveProjectUseCase::class.java
		).newInstance(
			ProjectsContainer.saveProjectUseCase
		)
	}
}
