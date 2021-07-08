package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.usecases.SetReviewStatusUseCase
import com.fiuba.seedyfiuba.projects.usecases.SetStageReviewStatusUseCase

class ReviewerDetailProjectViewModel(
	private val setReviewStatusUseCase: SetReviewStatusUseCase,
	private val setStageReviewStatusUseCase: SetStageReviewStatusUseCase
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

	fun setStageStatus(status: String) {
		launch {
			val project = project.value!!
			val projectId = project.id
			val reviewerId = AuthenticationManager.session!!.user.userId
			val stageId =  project.currentStageId
			when (setStageReviewStatusUseCase.invoke(
				status = status,
				reviewerId = reviewerId,
				projectId = projectId,
				stageId = stageId
			)) {
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
