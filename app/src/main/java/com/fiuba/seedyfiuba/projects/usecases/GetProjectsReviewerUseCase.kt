package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository

class GetProjectsReviewerUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(
		reviewerId: String?,
		reviewsStatus: String
	) = projectsRepository.getReviewerProjects(reviewerId, reviewsStatus)
}
