package com.fiuba.seedyfiuba.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.usecases.GetProfileUseCase
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetProjectUseCase

class PushHandleViewModel(
	private val getProfileUseCase: GetProfileUseCase,
	private val getProjectUseCase: GetProjectUseCase
) : BaseViewModel() {
	private val _profile = MutableLiveData<Profile>()
	val profile: LiveData<Profile> = _profile

	private val _project = MutableLiveData<Project>()
	val project: LiveData<Project> = _project

	fun getProject(projectId: Int) {
		launch {
			when (val result = getProjectUseCase.invoke(projectId)) {
				is Result.Success -> {
					_project.postValue(result.data)
				}

				is Result.Error -> {
					_error.postValue(true)
				}
			}
		}
	}

	fun getProfile(userId: Int) {
		launch {
			when (val result = getProfileUseCase.invoke(userId)) {
				is Result.Success -> {
					_profile.postValue(result.data)
				}

				is Result.Error -> {
					_error.postValue(true)
				}
			}
		}
	}
}
