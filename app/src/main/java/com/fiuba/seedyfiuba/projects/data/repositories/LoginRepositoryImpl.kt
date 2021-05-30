package com.fiuba.seedyfiuba.projects.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class ProjectRepositoryImpl(
	private val remoteDataSource: ProjectRemoteDataSource
) : ProjectsRepository {
	override suspend fun updateProject(project: Project): Result<Boolean> {
		return remoteDataSource.updateProject(project)
	}

	override suspend fun saveProject(project: Project): Result<Boolean> {
		return remoteDataSource.saveProject(project)
	}

	override suspend fun deleteProject(project: Project): Result<Boolean> {
		return remoteDataSource.deleteProject(project)
	}

	override suspend fun getProjects(): Result<List<Project>> {
		return remoteDataSource.getProjects()
	}
}
