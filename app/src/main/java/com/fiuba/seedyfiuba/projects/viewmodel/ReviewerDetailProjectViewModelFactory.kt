package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.SetReviewStatusUseCase

class ReviewerDetailProjectViewModelFactory :
    ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			SetReviewStatusUseCase::class.java
		).newInstance(
            ProjectsContainer.setReviewStatusUseCase
		)
	}
}
