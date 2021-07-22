package com.fiuba.seedyfiuba.projects.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.SearchForm
import com.fiuba.seedyfiuba.projects.domain.SponsorDTO
import com.fiuba.seedyfiuba.projects.domain.StageStatusDTO

interface ProjectRemoteDataSource {
	suspend fun updateProject(project: Project): Result<Project>
	suspend fun saveProject(project: Project): Result<Project>
	suspend fun deleteProject(project: Project): Result<Project>
	suspend fun getProjects(): Result<List<Project>>
	suspend fun getProject(projectId: String): Result<Project>
	suspend fun getProjects(status: String): Result<List<Project>>
	suspend fun search(searchForm: SearchForm): Result<List<Project>>
	suspend fun setReviewer(reviewerPostRequest: ReviewerPostRequest): Result<ReviewerResponse>
	suspend fun setReviewStatus(reviewerPutRequest: ReviewerPutRequest, reviewId:Int): Result<ReviewerResponse>
	suspend fun getProjectsReviewer(reviewerId: String?, reviewsStatus: List<String>?): Result<ReviewerListResponse>
	suspend fun sponsorProject(sponsorDTO: SponsorDTO, projectId: String): Result<Unit>
	suspend fun setStageReviewStatus(
		sponsorDTO: StageStatusDTO,
		projectId: String,
		stageId: String
	): Result<ReviewerResponse>
}
