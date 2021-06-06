package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.Stages
import com.fiuba.seedyfiuba.projects.usecases.SaveProjectUseCase
import java.math.BigDecimal

class StageProjectViewModel(
	private val saveProjectUseCase: SaveProjectUseCase
) : BaseViewModel() {
	private val _stages = MutableLiveData<MutableList<Stages>>(mutableListOf())
	val stages: LiveData<MutableList<Stages>> = _stages

	var project: Project = Project.newInstance()

	private val _projectResult = MutableLiveData<Project>()
	val projectResult: LiveData<Project> = _projectResult

	private val _isAddValid = MutableLiveData<Boolean>()
	val isAddValid: LiveData<Boolean> = _isAddValid

	private val _isContinueValid = MutableLiveData<Boolean>()
	val isContinueValid: LiveData<Boolean> = _isContinueValid

	fun setupWith(project: Project) {
		this.project = project
	}

	fun saveProject() {
		launch {
			mShowLoading.postValue(true)
			project = project.copy(stages = stages.value!!.toList())
			when (val result = saveProjectUseCase.invoke(project)) {
				is Result.Success -> {
					_projectResult.postValue(result.data)
					mShowLoading.postValue(false)
				}
				is Result.Error -> {
					mShowLoading.postValue(false)
					_error.postValue(true)
				}
			}
		}
	}

	fun validateProject() {
		_isContinueValid.postValue(stages.value?.isNotEmpty())
	}

	fun addStage(track: String, amount: BigDecimal) {
		val stage = Stages(track, amount)
		stages.value?.let {
			it.add(stage)
			_stages.postValue(it)
		}
	}

	fun validateStage(track: String, amount: BigDecimal) {
		if (track.isNotBlank() && amount > BigDecimal.ZERO) {
			_isAddValid.postValue(true)
		}
	}

	fun reorderStages(stages: List<Stages>) {
		_stages.postValue(stages.toMutableList())
	}

	fun stagesDrop(position: Int) {
		stages.value?.let {
			it.removeAt(position)
			_stages.postValue(it)
		}
	}
}
