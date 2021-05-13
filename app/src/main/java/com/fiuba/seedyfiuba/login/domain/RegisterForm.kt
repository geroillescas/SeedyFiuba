package com.fiuba.seedyfiuba.login.domain

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
	val usernameError: Int? = null,
	val passwordError: Int? = null,
	val passwordValidationError: Int? = null,
	val profileTypeError: Int? = null,
	val isDataValid: Boolean = false
)
