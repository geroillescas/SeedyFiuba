package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import com.google.gson.annotations.SerializedName

class HolaRequestDto(
	@SerializedName("last_name")
	val lastName: String
) : DTO()
