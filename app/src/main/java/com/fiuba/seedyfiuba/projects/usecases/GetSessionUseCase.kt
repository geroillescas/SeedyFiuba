package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository

class GetProjectsUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke() = projectsRepository.getProjects()
}

class GetProjectsByStateUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(state: String) = projectsRepository.getProjects(state)
}

class GetProjectUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(projectId: Int) = projectsRepository.getProject(projectId)
}


