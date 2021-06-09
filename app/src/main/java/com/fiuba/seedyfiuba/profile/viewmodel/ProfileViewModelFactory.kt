package com.fiuba.seedyfiuba.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.profile.ProfileContainer
import com.fiuba.seedyfiuba.profile.usecases.GetAllProfilesUseCase
import com.fiuba.seedyfiuba.profile.usecases.GetProfileUseCase
import com.fiuba.seedyfiuba.profile.usecases.SaveProfileUseCase
import com.fiuba.seedyfiuba.profile.usecases.UpdateProfileUseCase
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.SaveProjectUseCase
import com.fiuba.seedyfiuba.projects.usecases.UpdateProjectUseCase

/**
 * ViewModel provider factory to instantiate ProjectsViewModel.
 * Required given ProjectsViewModel has a non-empty constructor
 */
class ProfileViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			GetAllProfilesUseCase::class.java,
			GetProfileUseCase::class.java,
			SaveProfileUseCase::class.java
		).newInstance(
			ProfileContainer.getAllProfilesUseCase,
			ProfileContainer.getProfileUseCase,
			ProfileContainer.saveProfileUseCase
		)
	}
}
