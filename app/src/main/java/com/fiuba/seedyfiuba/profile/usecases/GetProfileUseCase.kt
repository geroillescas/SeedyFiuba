package com.fiuba.seedyfiuba.profile.usecases

import com.fiuba.seedyfiuba.profile.data.repositories.ProfileRepository

class GetProfileUseCase(private val profileRepository: ProfileRepository) {
	suspend fun invoke() = profileRepository.getProfile()
}
