package com.fiuba.seedyfiuba.profile.data.repositories


import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile

interface ProfileRepository {
	suspend fun getProfile(): Result<Profile>
	suspend fun getAllProfile(size: Int? = null, page: Int? = null): Result<List<Profile>>
	suspend fun saveProfile(profile: Profile): Result<Profile>
}
