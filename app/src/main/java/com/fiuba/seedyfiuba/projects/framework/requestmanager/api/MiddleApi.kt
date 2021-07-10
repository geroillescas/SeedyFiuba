package com.fiuba.seedyfiuba.projects.framework.requestmanager.api

import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.domain.SponsorDTO
import com.fiuba.seedyfiuba.projects.domain.StageStatusDTO
import retrofit2.Response
import retrofit2.http.*

interface MiddleApi {
	@GET(ProjectsConstant.END_POINT_REVIEWS)
	suspend fun getProjects(
		@Query(value = "reviewerId") reviewerId: String?,
		@Query(value = "status", encoded = true) status: List<String>?
	): Response<ReviewerListResponse>

	@POST(ProjectsConstant.END_POINT_REVIEWS)
	suspend fun saveReview(
		@Body reviewerPostRequest: ReviewerPostRequest
	): Response<ReviewerResponse>

	@PUT(ProjectsConstant.END_POINT_REVIEWS_ID)
	suspend fun updateReview(
		@Body reviewerPutRequest: ReviewerPutRequest,
		@Path(value = "reviewId") reviewId: Int
	): Response<ReviewerResponse>


	@POST(ProjectsConstant.END_POINT_PROJECT_FUND)
	suspend fun sponsorProject(
		@Body sponsorDTO: SponsorDTO,
		@Path(value = "projectId") projectId: String
	): Response<Unit>

	@POST(ProjectsConstant.END_POINT_PROJECT_STAGES)
	suspend fun updateStage(
		@Body stageStatusDTO: StageStatusDTO,
		@Path(value = "projectId") projectId: String,
		@Path(value = "stageId") stageId: String
	): Response<ReviewerResponse>

}
