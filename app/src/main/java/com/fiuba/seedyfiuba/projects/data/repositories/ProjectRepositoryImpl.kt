package com.fiuba.seedyfiuba.projects.data.repositories

import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.requestmanager.dto.BalanceResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.SearchForm
import com.fiuba.seedyfiuba.projects.domain.SponsorDTO
import com.fiuba.seedyfiuba.projects.domain.StageStatusDTO
import java.math.BigDecimal

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class ProjectRepositoryImpl(
	private val remoteDataSource: ProjectRemoteDataSource,
	private val profileRemoteDataSource: ProfileRemoteDataSource
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

	override suspend fun getProject(projectId: Int): Result<Project> {
		return remoteDataSource.getProject(projectId.toString())
	}

	override suspend fun getProjects(): Result<List<Project>> {
		return remoteDataSource.getProjects()
	}

	override suspend fun getProjects(status: String): Result<List<Project>> {
		return remoteDataSource.getProjects(status)
	}

	override suspend fun getReviewerProjects(reviewerId: String?, reviewsStatus: List<String>?): Result<List<Project>> {
		return when (val result = remoteDataSource.getProjectsReviewer(reviewerId, reviewsStatus)) {
			is Result.Success -> Result.Success(result.data.results.mapNotNull {
				it.project?.copy(review = it.review)
			})
			is Result.Error -> result
		}
	}

	override suspend fun search(searchForm: SearchForm): Result<List<Project>> {
		return remoteDataSource.search(searchForm)
	}

	override suspend fun getReviewersFilteredBy(
		profileType: ProfileType,
		size: Int?,
		page: Int?
	): Result<ProfilesListResponse> {
		return when (val result =
			profileRemoteDataSource.getReviewersFilteredBy(profileType, size, page)) {
			is Result.Success -> Result.Success(result.data)
			is Result.Error -> result
		}
	}

	override suspend fun setReviewer(reviewerId: Int, projectId: Int): Result<ReviewerResponse> {
		val reviewerPostRequest = ReviewerPostRequest(reviewerId, projectId)
		return remoteDataSource.setReviewer(reviewerPostRequest)
	}

	override suspend fun setReviewStatus(
		status: String,
		reviewId: Int
	): Result<ReviewerResponse> {
		val reviewerPutRequest = ReviewerPutRequest(status)
		return remoteDataSource.setReviewStatus(reviewerPutRequest, reviewId)
	}

	override suspend fun sponsor(amount: BigDecimal, projectId: Int): Result<Unit> {
		val sponsorDTO = SponsorDTO(amount, AuthenticationManager.session!!.user.userId)
		return remoteDataSource.sponsorProject(sponsorDTO, projectId.toString())

	}

	override suspend fun getBalance(userId: Int): Result<BigDecimal> {
		return when (val result = remoteDataSource.getBalance(userId)) {
			is Result.Success -> Result.Success(result.data.balance)
			is Result.Error -> result
		}
	}

	override suspend fun requestStageReview(project: Project): Result<Unit> {
		return remoteDataSource.requestStageReview(project)
	}

	override suspend fun setStageReviewStatus(
		status: String,
		reviewerId: Int,
		projectId: Int,
		stageId: Int
	): Result<ReviewerResponse> {
		val sponsorDTO = StageStatusDTO(reviewerId)
		return remoteDataSource.setStageReviewStatus(sponsorDTO, projectId.toString(), stageId.toString())
	}
}
