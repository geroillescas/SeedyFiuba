package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.domain.Project

class SaveProjectUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(project: Project) = projectsRepository.saveProject(project)
}
