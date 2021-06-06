package com.fiuba.seedyfiuba.projects.data.repositories


import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.fragments.SearchForm

interface ProjectsRepository {
	suspend fun updateProject(project: Project): Result<Project>
	suspend fun saveProject(project: Project): Result<Project>
	suspend fun deleteProject(project: Project): Result<Project>
	suspend fun getProjects(): Result<List<Project>>
	suspend fun search(searchForm: SearchForm): Result<List<Project>>
}
