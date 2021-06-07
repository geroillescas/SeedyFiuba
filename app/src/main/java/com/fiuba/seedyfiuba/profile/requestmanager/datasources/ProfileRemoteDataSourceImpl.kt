package com.fiuba.seedyfiuba.profile.requestmanager.datasources

import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileApi
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class ProfileRemoteDataSourceImpl(private val projectApi: ProfileApi) : RemoteBaseDataSource(),
	ProfileRemoteDataSource {

	override suspend fun getProfile(): Result<Profile> {
		TODO("Not yet implemented")
	}

	override suspend fun getAllProfile(): Result<List<Profile>> {
		TODO("Not yet implemented")
	}

	override suspend fun saveProfile(profile: Profile): Result<Profile> {
		TODO("Not yet implemented")
	}

}


