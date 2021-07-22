package com.fiuba.seedyfiuba.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.profile.ProfileContainer
import com.fiuba.seedyfiuba.profile.usecases.*
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.GetProjectUseCase
import com.fiuba.seedyfiuba.projects.usecases.SaveProjectUseCase
import com.fiuba.seedyfiuba.projects.usecases.UpdateProjectUseCase

/**
 * ViewModel provider factory to instantiate ProjectsViewModel.
 * Required given ProjectsViewModel has a non-empty constructor
 */
class PushHandlerViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			GetProfileUseCase::class.java,
			GetProjectUseCase::class.java
		).newInstance(
			ProfileContainer.getProfileUseCase,
			ProjectsContainer.getProjectUseCase
		)
	}
}
