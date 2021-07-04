package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository

class GetReviewerUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke( size: Int? = null, page: Int? = null) = projectsRepository.getReviewersFilteredBy(
        ProfileType.VEEDOR, size, page)
}
