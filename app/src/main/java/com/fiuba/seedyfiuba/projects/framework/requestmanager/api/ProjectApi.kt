package com.fiuba.seedyfiuba.projects.framework.requestmanager.api

import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.ProjectRequestDTO
import com.fiuba.seedyfiuba.projects.domain.ProjectUpdateDTO
import com.fiuba.seedyfiuba.projects.domain.SearchResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface ProjectApi {
	@GET(ProjectsConstant.END_POINT_PROJECTS)
	suspend fun getProjects(
		@Query(value = "status") status: String?
	): Response<List<Project>>

	@GET(ProjectsConstant.END_POINT_PROJECT_ID)
	suspend fun getProject(
		@Path(value = "id") id: String
	): Response<List<Project>>

	@GET(ProjectsConstant.END_POINT_PROJECT_SEARCH)
	suspend fun searchProject(
		@Header(value = "x-auth-token") token: String,
		@Query(value = "hashtags") hashtag: String?,
		@Query(value = "category") category: String?,
		@Query(value = "status") status: String?,
		@Query(value = "locationX") locationX: Double?,
		@Query(value = "locationY") locationY: Double?
	): Response<SearchResponseDTO>

	@POST(ProjectsConstant.END_POINT_PROJECTS)
	suspend fun saveProject(
		@Body project: ProjectRequestDTO
	): Response<Project>

	@PUT(ProjectsConstant.END_POINT_PROJECT_ID)
	suspend fun updateProject(
		@Body project: ProjectUpdateDTO,
		@Path(value = "id") projectId: String
	): Response<Project>
}


