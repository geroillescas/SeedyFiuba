package com.fiuba.seedyfiuba.projects.domain

import com.google.gson.annotations.SerializedName

enum class ProjectStatus(val value: String) {
	@SerializedName("created")
	CREATED("Iniciado"),
	@SerializedName("pending-reviewer")
	PENDING_REVIEWER_CONFIRMATION("Pendiente de confirmacion de veedor"),
	@SerializedName("in-progress")
	IN_PROGRESS("En progreso"),
	@SerializedName("done")
	FINISHED("Terminado"),
	NONE("Ninguno")
}
