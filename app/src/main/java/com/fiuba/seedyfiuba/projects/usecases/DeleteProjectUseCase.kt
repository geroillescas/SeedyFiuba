package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.domain.Project

class DeleteProjectUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(project: Project) = projectsRepository.deleteProject(project)
}
