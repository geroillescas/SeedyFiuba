package com.fiuba.seedyfiuba.projects.framework.requestmanager.api

import com.fiuba.seedyfiuba.funds.domains.ContractsListResponse
import com.fiuba.seedyfiuba.funds.domains.TransferRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.BalanceResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.domain.SponsorDTO
import com.fiuba.seedyfiuba.projects.domain.StageStatusDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET(ProjectsConstant.END_POINT_PROJECT_CONTRACTS)
    suspend fun getFunds(
        @Query(value = "size") size: Int? = null,
        @Query(value = "page") page: Int? = null,
        @Query(value = "funderId") funderId: Int? = null
    ): Response<ContractsListResponse>

    @GET(ProjectsConstant.END_POINT_WALLET)
    suspend fun getBalance(
        @Path(value = "userId") userId: Int
    ): Response<BalanceResponse>


    @POST(ProjectsConstant.END_POINT_PROJECT_REVIEW)
    suspend fun requestStageReview(
        @Path(value = "projectId") projectId: Int
    ): Response<Unit>

    @POST(ProjectsConstant.END_POINT_WALLET_TRANSFER)
    suspend fun transferFund(
        @Path(value = "userId") userId: Int,
        @Body transferRequest: TransferRequest
    ): Response<Unit>

}
