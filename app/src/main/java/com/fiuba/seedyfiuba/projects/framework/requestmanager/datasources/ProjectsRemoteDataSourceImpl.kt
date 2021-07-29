package com.fiuba.seedyfiuba.projects.framework.requestmanager.datasources

import android.content.Context
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.requestmanager.dto.BalanceResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.*
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.MiddleApi
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class ProjectsRemoteDataSourceImpl(
	private val projectApi: ProjectApi,
	private val middleApi: MiddleApi, context: Context
) : RemoteBaseDataSource(context),
	ProjectRemoteDataSource {
	override suspend fun updateProject(project: Project): Result<Project> {
		val projectRequestDTO = ProjectUpdateDTO(
			title = project.title,
			description = project.description,
			category = project.type,
			location = project.location,
			hashtags = project.hashtags,
			mediaUrls = project.mediaUrls
		)
		return getResult {
			projectApi.updateProject(
				projectRequestDTO,
				project.id.toString()
			)
		}
	}

	override suspend fun saveProject(project: Project): Result<Project> {
		val projectRequestDTO = ProjectRequestDTO(
			title = project.title,
			description = project.description,
			category = project.type,
			location = project.location,
			hashtags = project.hashtags,
			mediaUrls = project.mediaUrls,
			stages = project.stages,
			finishDate = project.finishDate,
			ownerId = AuthenticationManager.session!!.user.userId
		)
		return getResult {
			projectApi.saveProject(projectRequestDTO)
		}
	}

	override suspend fun deleteProject(project: Project): Result<Project> {
		TODO("Not yet implemented")
	}

	override suspend fun getProjects(): Result<List<Project>> {
		return getResult {
			projectApi.getProjects(null)
		}
	}

	override suspend fun getProjects(status: String): Result<List<Project>> {
		return getResult {
			projectApi.getProjects(status)
		}
	}

	override suspend fun getProject(projectId: String): Result<Project> {
		return getResult {
			projectApi.getProject(projectId)
		}
	}

	override suspend fun search(searchForm: SearchForm): Result<List<Project>> {
		return getResult {
			val hashtag = if (searchForm.hashtag?.isEmpty() != false) null else searchForm.hashtag
			projectApi.searchProject(
				AuthenticationManager.session!!.token,
				hashtag,
				searchForm.projectType?.name?.toLowerCase(),
				searchForm.projectStatus?.name?.toLowerCase(),
				searchForm.location?.x?.toDouble(),
				searchForm.location?.y?.toDouble()
			)
		}.let {
			when (val result = it) {
				is Result.Success -> Result.Success(result.data.results)
				is Result.Error -> Result.Error(result.exception)
			}
		}
	}

	override suspend fun setReviewer(reviewerPostRequest: ReviewerPostRequest): Result<ReviewerResponse> {
		return getResult {
			middleApi.saveReview(reviewerPostRequest)
		}

	}

	override suspend fun setReviewStatus(
		reviewerPutRequest: ReviewerPutRequest,
		reviewId: Int
	): Result<ReviewerResponse> {
		return getResult {
			middleApi.updateReview(reviewerPutRequest, reviewId)
		}
	}

	override suspend fun getProjectsReviewer(reviewerId: String?, reviewsStatus: List<String>?): Result<ReviewerListResponse> {
		return getResult {
			middleApi.getProjects(reviewerId, reviewsStatus)
		}
	}

	override suspend fun sponsorProject(sponsorDTO: SponsorDTO, projectId: String): Result<Unit> {
		return getResult {
			middleApi.sponsorProject(sponsorDTO, projectId)
		}
	}

	override suspend fun setStageReviewStatus(
		sponsorDTO: StageStatusDTO,
		projectId: String,
		stageId: String
	): Result<ReviewerResponse> {
		return getResult {
			middleApi.updateStage(
				sponsorDTO,
				projectId,
				stageId
			)
		}
	}

	override suspend fun getBalance(userId: Int): Result<BalanceResponse> {
		return getResult {
			middleApi.getBalance(userId)
		}
	}

	override suspend fun requestStageReview(project: Project): Result<Unit> {
		return getResult {
			middleApi.requestStageReview(project.id)
		}
	}
}


