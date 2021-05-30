package com.fiuba.seedyfiuba.login.domain

import com.google.gson.annotations.SerializedName

enum class ProfileType(val value: String) {
	@SerializedName("sponsor")
	PATROCINADOR("sponsor"),

	@SerializedName("entrepreneur")
	EMPRENDEDOR("entrepreneur"),

	@SerializedName("reviewer")
	VEEDOR("reviewer"),

	UNKNOWN("-")
}

