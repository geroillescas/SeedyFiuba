package com.fiuba.seedyfiuba.login.data.repositories

import android.util.Log
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.data.datasources.RemoteHolaMundoDataSource
import com.fiuba.seedyfiuba.login.domain.HolaMundoModel
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaMundoDto
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaRequestDto

class HolaMundoRepositoryImpl(
	private val remoteHolaMundoDataSource: RemoteHolaMundoDataSource
) : HolaMundoRepository {
	override suspend fun getHolaMundo(
		name: String,
		fullName: String
	): Result<HolaMundoModel> {
		Log.i("Seedy", "getHola")
		return when (val result =
			remoteHolaMundoDataSource.getHola(name, HolaRequestDto(fullName))) {
			is Result.Success -> Result.Success(
				result.data.map(
					HolaMundoDto.mapToDomain,
					result.data
				) as HolaMundoModel
			)
			is Result.Error -> result
		}
	}
}
