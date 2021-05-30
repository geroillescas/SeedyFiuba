package com.fiuba.seedyfiuba.projects.framework.requestmanager.datasources

import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class ProjectsRemoteDataSourceImpl(private val projectApi: ProjectApi) : RemoteBaseDataSource(),
	ProjectRemoteDataSource {
	override suspend fun updateProject(project: Project): Result<Boolean> {
		TODO("Not yet implemented")
	}

	override suspend fun saveProject(project: Project): Result<Boolean> {
		TODO("Not yet implemented")
	}

	override suspend fun deleteProject(project: Project): Result<Boolean> {
		TODO("Not yet implemented")
	}

	override suspend fun getProjects(): Result<List<Project>> {
		TODO("Not yet implemented")
	}

}


