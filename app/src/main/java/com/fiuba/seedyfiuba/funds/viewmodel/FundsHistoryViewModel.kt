package com.fiuba.seedyfiuba.funds.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.funds.domains.Contract
import com.fiuba.seedyfiuba.funds.domains.ContractResponse
import com.fiuba.seedyfiuba.funds.usecases.GetFundsUseCase
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetProjectUseCase

class FundsHistoryViewModel(
	private val getFundsUseCase: GetFundsUseCase,
	private val getProjectUseCase: GetProjectUseCase
) : BaseViewModel() {
	private val _contractList = MutableLiveData<List<ContractResponse>>()
	val contractListLiveData: LiveData<List<ContractResponse>> = _contractList

	private val _projectList = MutableLiveData<List<Project>>()

	private val _project = MutableLiveData<Project>()
	val project: LiveData<Project> = _project

	private var page = 0
	private var size = 10
	private var max = 0
	private var fetched = 0

	fun getContracts() {
		if (fetched < max || fetched == 0) {
			launch {
				val funderId = AuthenticationManager.userId
				when (val result = getFundsUseCase.invoke(
					size = size,
					page = page,
					funderId = funderId
				)) {
					is Result.Success -> {
						page++
						fetched += result.data.results.size
						max = result.data.totalItems
						val contracts = result.data.results
						val projects = result.data.results.map { it.project }
						_contractList.value?.toMutableList()?.let {
							it.addAll(contracts)
							_contractList.postValue(it)
						} ?: run {
							_contractList.postValue(contracts)
						}

						_projectList.value?.toMutableList()?.let {
							it.addAll(projects)
							_projectList.postValue(it)
						} ?: run {
							_projectList.postValue(projects)
						}
					}
					is Result.Error -> {
					}
				}
			}
		}
	}

	fun getProject(projectId: Int) {
		launch {
			val project = _projectList.value?.find { it.id == projectId }
			_project.postValue(project!!)
		}
	}
}
