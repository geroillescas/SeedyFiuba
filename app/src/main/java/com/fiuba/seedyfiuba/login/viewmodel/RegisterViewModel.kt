package com.fiuba.seedyfiuba.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.commons.SeedyFiubaError
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.login.domain.RegisterForm
import com.fiuba.seedyfiuba.login.domain.RegisterFormState
import com.fiuba.seedyfiuba.login.domain.RegisteredInUser
import com.fiuba.seedyfiuba.login.usecases.RegisterUseCase
import java.net.HttpURLConnection

class RegisterViewModel(
	private val registerUseCase: RegisterUseCase
) : BaseViewModel() {

	private val _registerFormState = MutableLiveData<RegisterFormState>()
	val registerFormState: LiveData<RegisterFormState> = _registerFormState

	private val _registerResult = MutableLiveData<RegisteredInUser>()
	val registerResult: LiveData<RegisteredInUser> = _registerResult

	fun register(
		name: String,
		lastname: String,
		email: String,
		password: String,
		profileType: String
	) {
		// can be launched in a separate asynchronous job
		launch {
			val profileTypeEnum = when (profileType) {
				"Patrocinador" -> ProfileType.PATROCINADOR
				"Emprendedor" -> ProfileType.EMPRENDEDOR
				else -> ProfileType.VEEDOR
			}
			val registerForm = RegisterForm(name, lastname, email, password, profileTypeEnum)
			when (val result = registerUseCase.invoke(registerForm)) {
				is Result.Success -> _registerResult.postValue(result.data)
				is Result.Error -> {
					when (result.exception) {
						is SeedyFiubaError.UnknownSeedyFiubaApiException -> {
							if (result.exception.code == HttpURLConnection.HTTP_CONFLICT) {
								_registerFormState.postValue(
									RegisterFormState(
										emailError = R.string.invalid_email_already_used
									)
								)
							} else {
								_error.value = true
							}
						}
						else -> _error.value = true
					}
				}
			}
		}
	}

	fun registerDataChanged(
		name: String,
		lastName: String,
		email: String,
		password: String,
		passwordValidation: String,
		profileType: String
	) {
		when {
			!name.isNotBlank() -> {
				_registerFormState.value =
					RegisterFormState(
						nameError = R.string.invalid_username
					)
			}
			!lastName.isNotBlank() -> {
				_registerFormState.value =
					RegisterFormState(
						lastNameError = R.string.invalid_lastname
					)
			}

			!isEmailNameValid(email) -> {
				_registerFormState.value =
					RegisterFormState(
						emailError = R.string.invalid_email
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

	fun registerNameChanged(name: String) {
		when {
			!name.isNotBlank() -> {
				_registerFormState.value = RegisterFormState(nameError = R.string.invalid_username)
			}
		}
	}

	fun registerLastNameChanged(lastName: String) {
		when {
			!lastName.isNotBlank() -> {
				_registerFormState.value =
					RegisterFormState(lastNameError = R.string.invalid_lastname)
			}
		}
	}

	fun registerEmailChanged(email: String) {
		if (!isEmailNameValid(email)) {
			_registerFormState.value = RegisterFormState(emailError = R.string.invalid_email)
		} else {
			_registerFormState.value = RegisterFormState(emailError = 0)
		}
	}

	fun registerPassswordChanged(password: String) {
		when {
			!isPasswordValid(password) -> {
				_registerFormState.value =
					RegisterFormState(passwordError = R.string.invalid_password)
			}
		}
	}

	// A placeholder username validation check
	private fun isEmailNameValid(email: String): Boolean {
		return email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches()
	}

	// A placeholder password validation check
	private fun isPasswordValid(password: String): Boolean {
		return password.length > 6
	}

	// A placeholder password validation check
	private fun isPasswordValidationValid(password: String, passwordValidation: String): Boolean {
		return password == passwordValidation
	}
}
