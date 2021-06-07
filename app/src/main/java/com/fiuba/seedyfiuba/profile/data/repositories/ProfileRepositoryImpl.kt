package com.fiuba.seedyfiuba.profile.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.fragments.SearchForm

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

	override suspend fun getAllProfile(): Result<List<Profile>> {
		return remoteDataSource.getAllProfile()
	}

	override suspend fun saveProfile(profile: Profile): Result<Profile> {
		return remoteDataSource.saveProfile(profile)
	}

}
