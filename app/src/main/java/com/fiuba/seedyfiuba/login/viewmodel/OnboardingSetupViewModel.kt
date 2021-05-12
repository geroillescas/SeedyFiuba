package com.fiuba.seedyfiuba.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.login.domain.RegisterFormState
import com.fiuba.seedyfiuba.login.usecases.GetSessionUseCase

class OnboardingSetupViewModel(private val getSessionUseCase: GetSessionUseCase) : BaseViewModel() {

	private val _registerFormState = MutableLiveData<RegisterFormState>()
	val registerFormState: LiveData<RegisterFormState> = _registerFormState

	private val _finished = MutableLiveData<Boolean>()
	val finished: LiveData<Boolean> = _finished

	fun getSession() {
		launch {
			val session = getSessionUseCase.invoke()

			if (session.profileType == "Patrocinador") {
				mShowLoading.postValue(false)
			} else {
				mShowLoading.postValue(true)
				_finished.postValue(true)
			}
		}
	}

	fun continueFlow() {
		_finished.postValue(true)
	}

	fun setup(locale: String, projectType: String) {
		launch {
			_finished.postValue(true)
		}
	}
}
