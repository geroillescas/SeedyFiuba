package com.fiuba.seedyfiuba.projects.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.fiuba.seedyfiuba.projects.domain.SearchForm

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

	override suspend fun getProjects(): Result<List<Project>> {
		return remoteDataSource.getProjects()
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
		val reviewerPostRequest = ReviewerPostRequest(reviewerId.toLong(), projectId.toLong())
		return remoteDataSource.setReviewer(reviewerPostRequest)
	}

	override suspend fun setReviewStatus(
		status: ProjectStatus,
		projectId: Int
	): Result<ReviewerResponse> {
		val reviewerPutRequest = ReviewerPutRequest(status.value)
		return remoteDataSource.setReviewStatus(reviewerPutRequest, projectId.toString())
	}
}
