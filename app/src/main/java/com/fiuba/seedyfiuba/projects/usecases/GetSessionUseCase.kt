package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.domain.Project

class GetProjectsUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke() = projectsRepository.getProjects()
}

class SaveProjectUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(project: Project) = projectsRepository.saveProject(project)
}

class UpdateProjectUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(project: Project) = projectsRepository.updateProject(project)
}

class DeleteProjectUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(project: Project) = projectsRepository.deleteProject(project)
}
