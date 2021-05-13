package com.fiuba.seedyfiuba.login.domain

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
	val success: Session? = null,
	val error: Int? = null
)

