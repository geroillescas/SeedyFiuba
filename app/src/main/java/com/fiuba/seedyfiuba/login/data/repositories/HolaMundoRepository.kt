package com.fiuba.seedyfiuba.login.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.HolaMundoModel

interface HolaMundoRepository {
	suspend fun getHolaMundo(name: String, fullName: String): Result<HolaMundoModel>
}
