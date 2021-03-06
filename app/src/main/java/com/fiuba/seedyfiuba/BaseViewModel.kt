package com.fiuba.seedyfiuba

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.domain.Session
import kotlinx.coroutines.*
import java.util.logging.Level
import java.util.logging.Logger

open class BaseViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
	ViewModel() {
	private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		Logger.getGlobal().log(Level.ALL, throwable.message ?: "")
		Log.e(BaseViewModel::class.simpleName, throwable.message ?: "")
	}

	protected val _session = MutableLiveData<Session>()
	val session: LiveData<Session> = _session

	protected val mShowLoading = MutableLiveData<Boolean>()
	val showLoading: LiveData<Boolean> = mShowLoading

	protected val _error = MutableLiveData<Boolean>()
	val showError: LiveData<Boolean> = _error

	fun launch(block: suspend CoroutineScope.() -> Unit) {
		viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
			mShowLoading.postValue(true)

			withContext(Dispatchers.IO) {
				block()
			}

			mShowLoading.postValue(false)
		}
	}

	fun launchWithoutLoading(block: suspend CoroutineScope.() -> Unit) {
		viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
			withContext(Dispatchers.IO) {
				block()
			}
		}
	}

	fun getLocalSession() {
		launch {
			_session.postValue(LoginContainer.getSessionUseCase.invoke())
		}
	}
}
