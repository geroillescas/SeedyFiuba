package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository

class SetReviewStatusUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(
        status: String,
        reviewId: Int
    ) = projectsRepository.setReviewStatus(status, reviewId)
}

