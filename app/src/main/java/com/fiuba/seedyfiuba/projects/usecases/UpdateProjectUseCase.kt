package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.domain.Project

class UpdateProjectUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(project: Project) = projectsRepository.updateProject(project)
}
