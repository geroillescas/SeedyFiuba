package com.fiuba.seedyfiuba.projects.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetBalanceUseCase
import com.fiuba.seedyfiuba.projects.usecases.SponsorUseCase
import java.math.BigDecimal

open class SponsorViewModel(
	private val sponsorUseCase: SponsorUseCase,
	private val getBalanceUseCase: GetBalanceUseCase
	) : BaseViewModel() {

	private val _updated = MutableLiveData<Boolean>()
	val updated: LiveData<Boolean> = _updated

	private val _amountAvailable = MutableLiveData<BigDecimal>()
	val amountAvailable: LiveData<BigDecimal> = _amountAvailable

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

	fun getAmountAvailable(){
		launch {
			val userId = AuthenticationManager.userId
			when (val result = getBalanceUseCase.invoke(userId)) {
				is Result.Success -> {
					_amountAvailable.postValue(result.data)
				}
				is Result.Error -> {
					_error.postValue(true)
				}
			}
		}
	}

	fun isValidAmount(amount: Editable?): Boolean {
		amount?.let {
			return it.isNotEmpty() && it.toString().toBigDecimal() <= _amountAvailable.value
		} ?: run  {
			return false
		}
	}

	var project :  Project = Project.newInstance()


}
