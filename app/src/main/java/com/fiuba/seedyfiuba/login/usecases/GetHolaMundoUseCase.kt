package com.fiuba.seedyfiuba.login.usecases

import com.fiuba.seedyfiuba.login.data.repositories.HolaMundoRepository

class GetHolaMundoUseCase(private val holaMundoRepository: HolaMundoRepository) {
	suspend fun invoke(name: String, fullName: String) =
		holaMundoRepository.getHolaMundo(name, fullName)
}
