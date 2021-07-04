package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository

class SaveReviewerUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(reviewerId: Int, projectId: Int) = projectsRepository.setReviewer(reviewerId, projectId)
}
