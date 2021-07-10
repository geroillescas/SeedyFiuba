package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.usecases.SetReviewStatusUseCase
import com.fiuba.seedyfiuba.projects.usecases.SetStageReviewStatusUseCase

class ReviewerDetailProjectViewModelFactory :
    ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			SetReviewStatusUseCase::class.java,
			SetStageReviewStatusUseCase::class.java
		).newInstance(
            ProjectsContainer.setReviewStatusUseCase,
			ProjectsContainer.setStageReviewStatusUseCase
		)
	}
}
