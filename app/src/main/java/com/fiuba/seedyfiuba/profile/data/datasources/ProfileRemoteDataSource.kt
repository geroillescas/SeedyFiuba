package com.fiuba.seedyfiuba.profile.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.domain.ProfileTokenUpdateDTO
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse

interface ProfileRemoteDataSource {
	suspend fun getProfile(userId: Int): Result<Profile>
	suspend fun getReviewersFilteredBy(profileType: ProfileType, size: Int? = null, page: Int? = null): Result<ProfilesListResponse>
	suspend fun getAllProfile(size: Int? = null, page: Int? = null): Result<ProfilesListResponse>
	suspend fun saveProfile(profile: Profile): Result<Profile>
	suspend fun saveProfileTokenUpdateDTO(profileTokenUpdateDTO: ProfileTokenUpdateDTO): Result<Unit>
}
