package com.fiuba.seedyfiuba.profile.usecases

import com.fiuba.seedyfiuba.profile.data.repositories.ProfileRepository
import com.fiuba.seedyfiuba.profile.domain.Profile

class SaveProfileUseCase(private val profileRepository: ProfileRepository) {
	suspend fun invoke(profile: Profile) = profileRepository.saveProfile(profile)
}
