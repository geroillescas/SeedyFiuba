package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.fiuba.seedyfiuba.projects.usecases.SetReviewStatusUseCase

class ReviewerDetailProjectViewModel(
	private val setReviewStatusUseCase: SetReviewStatusUseCase
) : DetailProjectViewModel() {
	private val _updated = MutableLiveData<Boolean>()
	val updated: LiveData<Boolean> = _updated


	fun setReviewStatus(status: String) {
		launch {
			when (setReviewStatusUseCase.invoke(status, project.value?.review?.id!!)) {
				is Result.Success -> {
					_updated.postValue(true)
				}
				else -> {
					_error.postValue(true)
				}
			}
		}
	}
}
