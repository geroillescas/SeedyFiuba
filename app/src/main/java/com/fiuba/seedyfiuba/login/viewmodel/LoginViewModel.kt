package com.fiuba.seedyfiuba.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.commons.SeedyFiubaError
import com.fiuba.seedyfiuba.login.domain.LoginFormState
import com.fiuba.seedyfiuba.login.domain.LoginResult
import com.fiuba.seedyfiuba.login.usecases.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : BaseViewModel() {

	private val _loginForm = MutableLiveData<LoginFormState>()
	val loginFormState: LiveData<LoginFormState> = _loginForm

	private val _loginResult = MutableLiveData<LoginResult>()
	val loginResult: LiveData<LoginResult> = _loginResult

	fun login(username: String, password: String) {
		// can be launched in a separate asynchronous job
		launch {
			when (val result = loginUseCase.invoke(username, password)) {
				is Result.Success -> {
					_loginResult.postValue(
						LoginResult(
							success = result.data
						)
					)
				}
				is Result.Error -> {
					when (result.exception) {
						is SeedyFiubaError.UnknownSeedyFiubaApiException -> {
							if (result.exception.code == 400) {
								_loginResult.postValue(LoginResult(error = R.string.email_invalid))
								_loginForm.postValue(LoginFormState(usernameError = R.string.email_invalid))
							}


							if (result.exception.code == 401) {
								_loginResult.postValue(LoginResult(error = R.string.password_invalid))
								_loginForm.postValue(LoginFormState(usernameError = R.string.password_invalid))
							}

						}
						else -> {
							_loginResult.postValue(
								LoginResult(
									error = R.string.login_failed
								)
							)
						}
					}
				}
			}
		}
	}


	fun loginGoogle(token: String) {
		launch {
			val result = loginUseCase.invoke(token)

			if (result is Result.Success) {
				_loginResult.postValue(
					LoginResult(
						success = result.data
					)
				)
			} else {
				_loginResult.postValue(
					LoginResult(
						error = R.string.login_failed
					)
				)
			}
		}
	}

	fun loginDataChanged(email: String, password: String) {
		if (!isEmailNameValid(email)) {
			_loginForm.value =
				LoginFormState(
					usernameError = R.string.invalid_email
				)
		} else if (!isPasswordValid(password)) {
			_loginForm.value =
				LoginFormState(
					passwordError = R.string.invalid_password
				)
		} else {
			_loginForm.value =
				LoginFormState(
					isDataValid = true
				)
		}
	}

	// A placeholder username validation check
	private fun isEmailNameValid(email: String): Boolean {
		return email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches()
	}

	// A placeholder password validation check
	private fun isPasswordValid(password: String): Boolean {
		return password.length > 5
	}
}
