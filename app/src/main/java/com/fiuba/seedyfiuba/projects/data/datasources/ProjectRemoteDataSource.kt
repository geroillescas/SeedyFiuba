package com.fiuba.seedyfiuba.projects.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPostRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerPutRequest
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.SearchForm

interface ProjectRemoteDataSource {
	suspend fun updateProject(project: Project): Result<Project>
	suspend fun saveProject(project: Project): Result<Project>
	suspend fun deleteProject(project: Project): Result<Project>
	suspend fun getProjects(): Result<List<Project>>
	suspend fun search(searchForm: SearchForm): Result<List<Project>>
	suspend fun setReviewer(reviewerPostRequest: ReviewerPostRequest): Result<ReviewerResponse>
	suspend fun setReviewStatus(reviewerPutRequest: ReviewerPutRequest, reviewId:String): Result<ReviewerResponse>
}
