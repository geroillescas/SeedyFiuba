package com.mercadopago.smartpos.recharge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.util.logging.Level
import java.util.logging.Logger

open class BaseViewModel : ViewModel() {
	private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		Logger.getGlobal().log(Level.ALL, throwable.message ?: "")
	}


	private val mShowLoading = MutableLiveData<Boolean>()
	val showLoading: LiveData<Boolean> = mShowLoading

	fun launch(block: suspend CoroutineScope.() -> Unit) {
		viewModelScope.launch(coroutineExceptionHandler) {
			mShowLoading.postValue(true)

			withContext(Dispatchers.IO) {
				block()
			}

			mShowLoading.postValue(false)
		}
	}
}
