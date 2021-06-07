package com.fiuba.seedyfiuba.profile.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile

interface ProfileRemoteDataSource {
	suspend fun getProfile(): Result<Profile>
	suspend fun getAllProfile(): Result<List<Profile>>
	suspend fun saveProfile(profile: Profile): Result<Profile>
}
