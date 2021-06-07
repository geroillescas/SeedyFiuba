package com.fiuba.seedyfiuba.profile.requestmanager.api

import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileConstant.END_POINT_PROFILES
import com.fiuba.seedyfiuba.projects.domain.Project
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
	@GET(END_POINT_PROFILES)
	suspend fun getProjects(
	): Response<List<Profile>>
}
