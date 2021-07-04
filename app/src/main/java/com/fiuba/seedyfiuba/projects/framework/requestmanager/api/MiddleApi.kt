package com.fiuba.seedyfiuba.projects.framework.requestmanager.api

import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.domain.Project
import retrofit2.Response
import retrofit2.http.*

interface MiddleApi {
	@GET(ProjectsConstant.END_POINT_REVIEWS)
	suspend fun getProjects(
	): Response<List<Project>>

	@POST(ProjectsConstant.END_POINT_REVIEWS)
	suspend fun saveReview(
		@Body reviewerPostRequest: ReviewerPostRequest
	): Response<ReviewerResponse>

	@PUT(ProjectsConstant.END_POINT_REVIEWS_ID)
	suspend fun updateReview(
		@Body reviewerPutRequest: ReviewerPutRequest,
		@Path(value = "id") reviewId: String
	): Response<ReviewerResponse>
}
