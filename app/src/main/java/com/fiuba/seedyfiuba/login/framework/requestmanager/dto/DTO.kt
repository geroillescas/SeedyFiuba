package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import com.fiuba.seedyfiuba.commons.Entity

abstract class DTO {

	fun map(mapper: (Entity) -> DTO, fromClass: Entity): DTO {
		return createFromDomain(mapper, fromClass)
	}

	fun map(mapper: (DTO) -> Entity, fromClass: DTO): Entity {
		return mapToDomain(mapper, fromClass)
	}

	private fun createFromDomain(mapper: (Entity) -> DTO, fromClass: Entity): DTO {
		return try {
			mapper(fromClass)
		} catch (e: Exception) {
			throw e
		}
	}

	private fun mapToDomain(mapper: (DTO) -> Entity, fromClass: DTO): Entity {
		return try {
			mapper(fromClass)
		} catch (e: Exception) {
			throw e
		}
	}
}
