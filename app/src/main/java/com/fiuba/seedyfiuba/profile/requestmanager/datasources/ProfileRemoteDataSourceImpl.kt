package com.fiuba.seedyfiuba.profile.requestmanager.datasources

import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileApi
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class ProfileRemoteDataSourceImpl(private val profileApi: ProfileApi) : RemoteBaseDataSource(),
	ProfileRemoteDataSource {

	override suspend fun getProfile(): Result<Profile> {
		val userId = AuthenticationManager.session!!.user.userId
		return getResult {
			profileApi.getProfile(
				userId = userId
			)
		}
	}

	override suspend fun getAllProfile(): Result<ProfilesListResponse> {
		return getResult {
			profileApi.getAllProfile()
		}
	}

	override suspend fun saveProfile(profile: Profile): Result<Profile> {
		val userId = AuthenticationManager.session!!.user.userId
		return getResult {
			profileApi.saveProfile(
				userId = userId,
				profile = profile
			)
		}
	}
}


