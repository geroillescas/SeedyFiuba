package com.fiuba.seedyfiuba.login.framework.requestmanager.api

import com.fiuba.seedyfiuba.login.framework.requestmanager.api.LoginConstant.END_POINT_HOLA_MUNDO
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaMundoDto
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.HolaRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HolamundoApi {

	/**
	 * Requests authorization to see if user has enough balance.
	 *
	 * @return A object containing whether the user has enough balance in a [BalanceDto]
	 */
	@GET(END_POINT_HOLA_MUNDO)
	suspend fun getHolaMundo(
		@Path("id") id: String,
		@Query("name") name: String
	): Response<HolaMundoDto>

}
