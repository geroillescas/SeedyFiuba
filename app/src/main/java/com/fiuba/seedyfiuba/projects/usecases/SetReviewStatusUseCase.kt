package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus

class SetReviewStatusUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(status: ProjectStatus, projectId: Int) = projectsRepository.setReviewStatus(status, projectId)
}