package com.fiuba.seedyfiuba.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.login.domain.RegisterFormState
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.usecases.RegisterUseCase
import com.fiuba.seedyfiuba.login.usecases.SaveSessionUseCase
import com.fiuba.seedyfiuba.login.view.activities.Result

class RegisterViewModel(
	private val registerUseCase: RegisterUseCase,
	private val saveSession: SaveSessionUseCase
) : BaseViewModel() {

	private val _registerFormState = MutableLiveData<RegisterFormState>()
	val registerFormState: LiveData<RegisterFormState> = _registerFormState

	private val _registerResult = MutableLiveData<Session>()
	val registerResult: LiveData<Session> = _registerResult

	fun register(username: String, password: String, profileType: String) {
		// can be launched in a separate asynchronous job
		launch {
			when (val result = registerUseCase.invoke(username, password, profileType)) {
				is Result.Success -> {
					saveSession.invoke(result.data)
					_registerResult.postValue(result.data)
				}
				is Result.Error -> _error.value = true
			}
		}
	}

	fun registerDataChanged(
		username: String,
		password: String,
		passwordValidation: String,
		profileType: String
	) {
		when {
			!isUserNameValid(username) -> {
				_registerFormState.value =
					RegisterFormState(
						usernameError = R.string.invalid_username
					)
			}

			!isPasswordValid(password) -> {
				_registerFormState.value =
					RegisterFormState(
						passwordError = R.string.invalid_password
					)
			}

			!isPasswordValidationValid(password, passwordValidation) -> {
				_registerFormState.value =
					RegisterFormState(
						passwordValidationError = R.string.register_invalid_password_validation
					)
			}

			!profileType.isNotBlank() -> {
				_registerFormState.value =
					RegisterFormState(
						profileTypeError = R.string.invalid_username
					)
			}

			else -> {
				_registerFormState.value =
					RegisterFormState(
						isDataValid = true
					)
			}
		}
	}

	// A placeholder username validation check
	private fun isUserNameValid(username: String): Boolean {
		return username.contains('@') && Patterns.EMAIL_ADDRESS.matcher(username).matches()
	}

	// A placeholder password validation check
	private fun isPasswordValid(password: String): Boolean {
		return password.length > 5
	}

	// A placeholder password validation check
	private fun isPasswordValidationValid(password: String, passwordValidation: String): Boolean {
		return password == passwordValidation
	}
}
