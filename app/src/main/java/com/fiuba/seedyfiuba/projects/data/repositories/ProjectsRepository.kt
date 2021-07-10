package com.fiuba.seedyfiuba.projects.data.repositories


import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.SearchForm
import java.math.BigDecimal

interface ProjectsRepository {
	suspend fun updateProject(project: Project): Result<Project>
	suspend fun saveProject(project: Project): Result<Project>
	suspend fun deleteProject(project: Project): Result<Project>
	suspend fun getProjects(): Result<List<Project>>
	suspend fun getProjects(status: String): Result<List<Project>>
	suspend fun getReviewerProjects(reviewerId: String?, reviewsStatus: List<String>?): Result<List<Project>>
	suspend fun search(searchForm: SearchForm): Result<List<Project>>
	suspend fun getReviewersFilteredBy(profileType: ProfileType, size: Int? = null, page: Int? = null): Result<ProfilesListResponse>
	suspend fun setReviewer(reviewerId: Int, projectId: Int): Result<ReviewerResponse>
	suspend fun setReviewStatus(
		status: String,
		reviewId: Int
	): Result<ReviewerResponse>
	suspend fun sponsor(amount: BigDecimal, projectId: Int): Result<Unit>
	suspend fun setStageReviewStatus(
		status: String,
		reviewerId: Int,
		projectId: Int,
		stageId: Int
	): Result<ReviewerResponse>
}
