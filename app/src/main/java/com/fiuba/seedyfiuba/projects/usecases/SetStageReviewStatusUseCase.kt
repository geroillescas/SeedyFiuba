package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository

class SetStageReviewStatusUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(
		status: String,
		reviewerId: Int,
		projectId: Int,
		stageId: Int

	) = projectsRepository.setStageReviewStatus(status, reviewerId, projectId, stageId)
}
