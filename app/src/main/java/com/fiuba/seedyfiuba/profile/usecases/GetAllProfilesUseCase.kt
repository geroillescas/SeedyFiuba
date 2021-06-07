package com.fiuba.seedyfiuba.profile.usecases

import com.fiuba.seedyfiuba.profile.data.repositories.ProfileRepository
import com.fiuba.seedyfiuba.profile.domain.Profile

class GetAllProfilesUseCase(private val profileRepository: ProfileRepository) {
	suspend fun invoke() = profileRepository.getAllProfile()
}
