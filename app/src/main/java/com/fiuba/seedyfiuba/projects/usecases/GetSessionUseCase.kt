package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository

class GetProjectsUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke() = projectsRepository.getProjects()
}

