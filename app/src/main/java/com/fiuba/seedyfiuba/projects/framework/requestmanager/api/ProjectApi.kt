package com.fiuba.seedyfiuba.projects.framework.requestmanager.api

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.Project
import retrofit2.http.*

interface ProjectApi {
	@GET(ProjectsConstant.END_POINT_PROJECTS)
	suspend fun getProjects(): Result<List<Project>>

	@GET(ProjectsConstant.END_POINT_PROJECT_ID)
	suspend fun getProject(
		@Path(value = "id") id: String
	): Result<List<Project>>

	@GET(ProjectsConstant.END_POINT_PROJECT_SEARCH)
	suspend fun searchProject(
	): Result<List<Project>>

	@POST(ProjectsConstant.END_POINT_PROJECTS)
	suspend fun saveProject(
		@Body project: Project
	): Result<Boolean>

	@PUT(ProjectsConstant.END_POINT_PROJECT_ID)
	suspend fun updateProject(
		@Body project: Project
	): Result<Boolean>
}
