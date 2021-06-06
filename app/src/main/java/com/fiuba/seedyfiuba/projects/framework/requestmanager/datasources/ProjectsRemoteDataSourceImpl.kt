package com.fiuba.seedyfiuba.projects.framework.requestmanager.datasources

import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.ProjectRequestDTO
import com.fiuba.seedyfiuba.projects.domain.ProjectUpdateDTO
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi
import com.fiuba.seedyfiuba.projects.view.fragments.SearchForm

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class ProjectsRemoteDataSourceImpl(private val projectApi: ProjectApi) : RemoteBaseDataSource(),
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
			projectApi.getProjects()
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
				searchForm.location?.latitude,
				searchForm.location?.longitude
			)
		}.let {
			when (val result = it) {
				is Result.Success -> Result.Success(result.data.results)
				is Result.Error -> Result.Error(result.exception)
			}
		}
	}
}


