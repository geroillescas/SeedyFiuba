package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsUseCase
import com.fiuba.seedyfiuba.projects.usecases.SearchProjectsUseCase
import com.fiuba.seedyfiuba.projects.view.fragments.SearchForm

class ProjectsViewModel(
	private val getProjectsUseCase: GetProjectsUseCase,
	private val searchProjectsUseCase: SearchProjectsUseCase
) : BaseViewModel() {

	private val _projects = MutableLiveData<List<Project>>()
	val projects: LiveData<List<Project>> = _projects

	fun getProjects() {
		launch {
			mShowLoading.postValue(true)
			when (val result = getProjectsUseCase.invoke()) {
				is Result.Success -> {
					mShowLoading.postValue(false)
					_projects.postValue(result.data.takeLast(3))
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
					_projects.postValue(result.data.takeLast(3))
				}
				is Result.Error -> {
					mShowLoading.postValue(false)
				}
			}
		}
	}


}
