package com.fiuba.seedyfiuba.profile.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse

interface ProfileRemoteDataSource {
	suspend fun getProfile(): Result<Profile>
	suspend fun getAllProfile(): Result<ProfilesListResponse>
	suspend fun saveProfile(profile: Profile): Result<Profile>
}
