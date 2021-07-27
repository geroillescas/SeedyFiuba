package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.domain.Project

class SaveReviewerUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(reviewerId: Int, projectId: Int) = projectsRepository.setReviewer(reviewerId, projectId)
}


class RequestStageReviewUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(project: Project) = projectsRepository.requestStageReview(project)
}