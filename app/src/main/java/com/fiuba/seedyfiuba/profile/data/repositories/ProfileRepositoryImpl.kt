package com.fiuba.seedyfiuba.profile.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.domain.ProfileTokenUpdateDTO
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class ProfileRepositoryImpl(
	private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
	override suspend fun getProfile(userId: Int): Result<Profile> {
		return remoteDataSource.getProfile(userId)
	}

	override suspend fun getAllProfile( size: Int?, page: Int?): Result<List<Profile>> {
		return when (val result = remoteDataSource.getAllProfile(size, page)) {
			is Result.Success -> Result.Success(result.data.users)
			is Result.Error -> result
		}
	}

	override suspend fun getProfilesFilteredBy(
		profileType: ProfileType,
		size: Int?,
		page: Int?
	): Result<ProfilesListResponse> {
		return remoteDataSource.getReviewersFilteredBy(profileType, size, page)
	}

	override suspend fun saveProfile(profile: Profile): Result<Profile> {
		return remoteDataSource.saveProfile(profile)
	}

	override suspend fun saveProfileTokenUpdateDTO(token: String): Result<Unit> {
		val profileTokenUpdateDTO = ProfileTokenUpdateDTO(token)
		return remoteDataSource.saveProfileTokenUpdateDTO(profileTokenUpdateDTO)
	}

}
