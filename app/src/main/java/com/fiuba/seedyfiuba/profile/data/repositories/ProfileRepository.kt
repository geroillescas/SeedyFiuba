package com.fiuba.seedyfiuba.profile.data.repositories


import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse

interface ProfileRepository {
	suspend fun getProfile(): Result<Profile>
	suspend fun getAllProfile(size: Int? = null, page: Int? = null): Result<List<Profile>>
	suspend fun getProfilesFilteredBy(profileType: ProfileType, size: Int? = null, page: Int? = null): Result<ProfilesListResponse>
	suspend fun saveProfile(profile: Profile): Result<Profile>
}
