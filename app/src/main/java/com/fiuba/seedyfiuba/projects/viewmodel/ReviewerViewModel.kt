package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetReviewerUseCase
import com.fiuba.seedyfiuba.projects.usecases.SaveReviewerUseCase

class ReviewerViewModel(
	private val getReviewerUseCase: GetReviewerUseCase,
	private val saveReviewerUseCase: SaveReviewerUseCase
) : BaseViewModel() {

	lateinit var project: Project
	private var page = 0
	private var size = 10
	private var max = 0
	private var fetched = 0
	private val _profiles = MutableLiveData<List<Profile>>()
	val profile: LiveData<List<Profile>> = _profiles

	private val _updated = MutableLiveData<Boolean>()
	val updated: LiveData<Boolean> = _updated

	private val _mShowLoadingSpinner = MutableLiveData<Boolean>()
	val showLoadingSpinner: LiveData<Boolean> = _mShowLoadingSpinner

	fun getProjects() {
		launch {
			when (val result = getReviewerUseCase.invoke(size, page)) {
				is Result.Success -> {
					page++
					fetched += result.data.users.size
					max = result.data.totalItems
					_profiles.postValue(result.data.users)
				}
				is Result.Error -> {
				}
			}
		}
	}

	fun updateProjectWithProfileSelected(profile: Profile) {
		launch {
			when (saveReviewerUseCase.invoke(profile.id, project.id)) {
				is Result.Success -> {
					_updated.postValue(true)
				}
				is Result.Error -> {
					_error.postValue(true)
				}
			}
		}
	}

	fun getMoreProjects() {
		launchWithoutLoading {
			if (fetched >= max) {
				return@launchWithoutLoading
			}

			_mShowLoadingSpinner.postValue(true)
			when (val result = getReviewerUseCase.invoke(size, page)) {
				is Result.Success -> {
					_mShowLoadingSpinner.postValue(false)
					page++
					fetched += result.data.users.size
					_profiles.postValue(result.data.users)
				}
				is Result.Error -> {
					_mShowLoadingSpinner.postValue(false)
				}
			}
		}
	}
}
