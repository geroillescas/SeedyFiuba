package com.fiuba.seedyfiuba.projects.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.fiuba.seedyfiuba.projects.domain.SearchForm
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsByStateUseCase
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsReviewerUseCase
import com.fiuba.seedyfiuba.projects.usecases.GetProjectsUseCase
import com.fiuba.seedyfiuba.projects.usecases.SearchProjectsUseCase

class ProjectsViewModel(
	private val getProjectsUseCase: GetProjectsUseCase,
	private val searchProjectsUseCase: SearchProjectsUseCase,
	private val getProjectsReviewerUseCase: GetProjectsReviewerUseCase,
	private val getProjectsByStateUseCase: GetProjectsByStateUseCase
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
					_projects.postValue(result.data)
				}
				is Result.Error -> {
					Log.e(ProjectsViewModel::class.simpleName, "Error retriving normal projects")
				}
			}
		}
	}

	private fun getReviewerProjects() {
		launch {
			when (val result = getProjectsReviewerUseCase.invoke(
				AuthenticationManager.session?.user?.userId.toString(),
				listOf("pending")
			)
				) {
				is Result.Success -> {
					_projects.postValue(result.data)
					getFundingprojects()
				}
				is Result.Error -> {
					Log.e(ProjectsViewModel::class.simpleName, "Error retriving reviewer projects")
				}
			}
		}
	}

	private suspend fun getFundingprojects(){
		when (val result = getProjectsByStateUseCase.invoke(
			"in-progress"
		)
			) {
			is Result.Success -> {
				val oldProjects = _projects.value?.toMutableList()!!
				val filtered = result.data.filter {
					it.reviewerId == AuthenticationManager.session?.user?.userId &&
							it.status == ProjectStatus.STAGE_PENDING_REVIEWER_CONFIRMATION
				}
				oldProjects.addAll(filtered)
				_projects.postValue(oldProjects)
			}
			is Result.Error -> {
				Log.e(ProjectsViewModel::class.simpleName, "Error retriving funding projects")
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
