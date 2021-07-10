package com.fiuba.seedyfiuba.projects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.SponsorUseCase
import java.math.BigDecimal

open class SponsorViewModel(private val sponsorUseCase: SponsorUseCase) : BaseViewModel() {

	private val _updated = MutableLiveData<Boolean>()
	val updated: LiveData<Boolean> = _updated

	fun sponsor(amount: BigDecimal) {
		launch {
			when (sponsorUseCase.invoke(amount, project.id)) {
				is Result.Success -> {
					_updated.postValue(true)
				}
				is Result.Error -> {
					_error.postValue(true)
				}
			}

		}
	}

	var project :  Project = Project.newInstance()


}
