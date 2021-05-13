package com.fiuba.seedyfiuba.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.HolaMundoModel
import com.fiuba.seedyfiuba.login.usecases.GetHolaMundoUseCase

class HolaMundoViewModel(
	private val getHolaMundoUseCase: GetHolaMundoUseCase
) : BaseViewModel() {

	private val holaMundoModel = MutableLiveData<HolaMundoModel>()
	val holaMundo: LiveData<HolaMundoModel> = holaMundoModel

	fun getHolaMundo(name: String, fullname: String) {
		launch {
			Log.i("Seedy", "getHolaMundo")
			when (val result = getHolaMundoUseCase.invoke(name, fullname)) {
				is Result.Success -> holaMundoModel.postValue(result.data)
				is Result.Error -> {
					Log.d("ERROR", "ERROR EN EL DATA")
				}
			}

		}
	}
}
