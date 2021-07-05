package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsUseCase
import com.fiuba.seedyfiuba.projects.usecases.SearchProjectsUseCase
import com.fiuba.seedyfiuba.projects.domain.SearchForm
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsReviewerUseCase

class ProjectsViewModel(
	private val getProjectsUseCase: GetProjectsUseCase,
	private val searchProjectsUseCase: SearchProjectsUseCase,
	private val getProjectsReviewerUseCase: GetProjectsReviewerUseCase
) : BaseViewModel() {

	private val _projects = MutableLiveData<List<Project>>()
	val projects: LiveData<List<Project>> = _projects

	fun getProjects() {
		if (AuthenticationManager.session?.user?.profileType == ProfileType.VEEDOR) {
			getReviewerProjects()
		} else {
			getNormalProjects()
		}

	}

	private fun getNormalProjects() {
		launch {
			mShowLoading.postValue(true)
			when (val result = getProjectsUseCase.invoke()) {
				is Result.Success -> {
					mShowLoading.postValue(false)
					_projects.postValue(result.data)
				}
				is Result.Error -> {
					mShowLoading.postValue(false)
				}
			}
		}
	}

	private fun getReviewerProjects() {
		launch {
			when (val result = getProjectsReviewerUseCase.invoke(
				AuthenticationManager.session?.user?.userId.toString(),
				"pending"
			)
				) {
				is Result.Success -> {
					mShowLoading.postValue(false)
					_projects.postValue(result.data)
				}
				is Result.Error -> {
					mShowLoading.postValue(false)
				}
			}
		}
	}

	fun searchProjects(searchForm: SearchForm) {
		launch {
			mShowLoading.postValue(true)
			when (val result = searchProjectsUseCase.invoke(searchForm)) {
				is Result.Success -> {
					mShowLoading.postValue(false)
					_projects.postValue(result.data)
				}
				is Result.Error -> {
					mShowLoading.postValue(false)
				}
			}
		}
	}


}
