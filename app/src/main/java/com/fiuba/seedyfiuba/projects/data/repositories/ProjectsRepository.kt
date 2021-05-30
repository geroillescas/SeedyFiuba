package com.fiuba.seedyfiuba.projects.data.repositories


import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project

interface ProjectsRepository {
	suspend fun updateProject(project: Project): Result<Boolean>
	suspend fun saveProject(project: Project): Result<Boolean>
	suspend fun deleteProject(project: Project): Result<Boolean>
	suspend fun getProjects(): Result<List<Project>>
}
