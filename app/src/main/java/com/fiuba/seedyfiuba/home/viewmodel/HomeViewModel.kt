package com.fiuba.seedyfiuba.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.login.domain.LoginResult
import com.fiuba.seedyfiuba.login.domain.Session

class HomeViewModel(
) : BaseViewModel() {

	private val _session = MutableLiveData<Session>()
	val session: LiveData<Session> = _session
}
