package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsUseCase

class ProjectsViewModel(
	private val getProjectsUseCase: GetProjectsUseCase
) : BaseViewModel() {

	private val _projects = MutableLiveData<List<Project>>()
	val projects: LiveData<List<Project>> = _projects

	fun getProjects() {
		launch {
			when (val result = getProjectsUseCase.invoke()) {
				is Result.Success -> {
					_projects.postValue(result.data)
				}
				is Result.Error -> {

				}
			}
		}
	}
}
