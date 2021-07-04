package com.fiuba.seedyfiuba.profile.requestmanager.api

import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileConstant.END_POINT_PROFILES
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileConstant.END_POINT_PROFILE_ID
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {
	@GET(END_POINT_PROFILES)
	suspend fun getAllProfile(
		@Query(value = "size") size: Int? = null,
		@Query(value = "page") page: Int? = null
	): Response<ProfilesListResponse>

	@GET(END_POINT_PROFILES)
	suspend fun getProfilesFilteredBy(
		@Query(value = "role") profileType: String
	): Response<ProfilesListResponse>

	@PUT(END_POINT_PROFILE_ID)
	suspend fun saveProfile(
		@Path(value = "id") userId: Int,
		@Body profile: Profile
	) : Response<Profile>

	@GET(END_POINT_PROFILE_ID)
	suspend fun getProfile(@Path(value = "id") userId: Int): Response<Profile>
}
