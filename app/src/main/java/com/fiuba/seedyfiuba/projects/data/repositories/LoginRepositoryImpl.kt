package com.fiuba.seedyfiuba.projects.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.SearchForm

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class ProjectRepositoryImpl(
	private val remoteDataSource: ProjectRemoteDataSource
) : ProjectsRepository {
	override suspend fun updateProject(project: Project): Result<Project> {
		return remoteDataSource.updateProject(project)
	}

	override suspend fun saveProject(project: Project): Result<Project> {
		return remoteDataSource.saveProject(project)
	}

	override suspend fun deleteProject(project: Project): Result<Project> {
		return remoteDataSource.deleteProject(project)
	}

	override suspend fun getProjects(): Result<List<Project>> {
		return remoteDataSource.getProjects()
	}

	override suspend fun search(searchForm: SearchForm): Result<List<Project>> {
		return remoteDataSource.search(searchForm)
	}
}
