package com.fiuba.seedyfiuba.login.domain

/**
 * Data validation state of the login form.
 */
data class RegisterForm(
	val name: String,
	val lastName: String,
	val email: String,
	val password: String,
	val profileType: ProfileType
)

