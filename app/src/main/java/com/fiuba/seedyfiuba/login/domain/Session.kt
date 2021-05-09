package com.fiuba.seedyfiuba.login.domain

data class Session(
	val tokenId: String,
	val loggedInUser: LoggedInUser,
	val profileType: String
)
