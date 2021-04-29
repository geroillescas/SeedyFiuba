package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import com.fiuba.seedyfiuba.login.domain.Entity
import com.fiuba.seedyfiuba.login.domain.HolaMundoModel
import com.google.gson.annotations.SerializedName

data class HolaMundoDto(
	@SerializedName("full_name")
	val fullName: String
) : DTO() {
	companion object {
		val mapToDomain: (DTO) -> Entity = { response ->
			response as HolaMundoDto
			HolaMundoModel(response.fullName)
		}
	}
}
