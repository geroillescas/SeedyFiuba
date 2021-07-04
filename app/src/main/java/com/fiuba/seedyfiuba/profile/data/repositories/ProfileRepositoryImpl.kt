package com.fiuba.seedyfiuba.profile.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.domain.Profile

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class ProfileRepositoryImpl(
	private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
	override suspend fun getProfile(): Result<Profile> {
		return remoteDataSource.getProfile()
	}

	override suspend fun getAllProfile( size: Int?, page: Int?): Result<List<Profile>> {
		return when (val result = remoteDataSource.getAllProfile(size, page)) {
			is Result.Success -> Result.Success(result.data.users)
			is Result.Error -> result
		}
	}

	override suspend fun saveProfile(profile: Profile): Result<Profile> {
		return remoteDataSource.saveProfile(profile)
	}

}
