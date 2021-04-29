package com.fiuba.seedyfiuba.login.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaMundoDto
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaRequestDto

interface RemoteHolaMundoDataSource {
	suspend fun getHola(name: String, holaRequestDto: HolaRequestDto): Result<HolaMundoDto>
}
