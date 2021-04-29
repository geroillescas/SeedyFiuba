package com.fiuba.seedyfiuba.login.framework.requestmanager.datasources

import android.util.Log
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.data.datasources.RemoteHolaMundoDataSource
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.HolamundoApi
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaMundoDto
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaRequestDto

class RemoteHolaMundoDataSourceImpl(private val holamundoApi: HolamundoApi) :
	RemoteHolaMundoDataSource,
	RemoteBaseDataSource() {

	override suspend fun getHola(
		name: String,
		holaRequestDto: HolaRequestDto
	): Result<HolaMundoDto> {
		return getResult {
			holamundoApi.getHolaMundo("345", name)
		}
	}
}
