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


	fun setReviewStatus(status: Boolean) {
		launch {
			mShowLoading.postValue(true)
			when (setReviewStatusUseCase.invoke(ProjectStatus.NONE, project.value?.id!!)) {
				is Result.Success -> {
					_updated.postValue(true)
					mShowLoading.postValue(false)
				}
				else -> {
					mShowLoading.postValue(false)
					_error.postValue(true)
				}
			}
		}
	}
}
