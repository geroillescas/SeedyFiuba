package com.fiuba.seedyfiuba.profile.requestmanager.api

import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileConstant.END_POINT_PROFILES
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
	@GET(END_POINT_PROFILES)
	suspend fun getAllProfile(): Response<ProfilesListResponse>
}
