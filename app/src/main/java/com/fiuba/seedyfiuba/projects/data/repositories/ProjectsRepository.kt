package com.fiuba.seedyfiuba.projects.data.repositories


import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ProfilesListResponse
import com.fiuba.seedyfiuba.profile.requestmanager.dto.ReviewerResponse
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.fiuba.seedyfiuba.projects.domain.SearchForm

interface ProjectsRepository {
	suspend fun updateProject(project: Project): Result<Project>
	suspend fun saveProject(project: Project): Result<Project>
	suspend fun deleteProject(project: Project): Result<Project>
	suspend fun getProjects(): Result<List<Project>>
	suspend fun search(searchForm: SearchForm): Result<List<Project>>
	suspend fun getReviewersFilteredBy(profileType: ProfileType, size: Int? = null, page: Int? = null): Result<ProfilesListResponse>
	suspend fun setReviewer(reviewerId: Int, projectId: Int): Result<ReviewerResponse>
	suspend fun setReviewStatus(status: ProjectStatus, projectId: Int): Result<ReviewerResponse>
}
