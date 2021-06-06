package com.fiuba.seedyfiuba.projects

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.data.repositories.ProjectRepositoryImpl
import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.framework.requestmanager.RequestManagerContainer
import com.fiuba.seedyfiuba.projects.framework.requestmanager.datasources.ProjectsRemoteDataSourceImpl
import com.fiuba.seedyfiuba.projects.usecases.*

object ProjectsContainer {

	private lateinit var context: Context

	//Use Cases
	val getProjectsUseCase: GetProjectsUseCase by lazy {
		GetProjectsUseCase(projectRepository)
	}

	val searchProjectsUseCase: SearchProjectsUseCase by lazy {
		SearchProjectsUseCase(projectRepository)
	}

	val saveProjectUseCase: SaveProjectUseCase by lazy {
		SaveProjectUseCase(projectRepository)
	}

	val updateProjectUseCase: UpdateProjectUseCase by lazy {
		UpdateProjectUseCase(projectRepository)
	}

	val deleteProjectUseCase: DeleteProjectUseCase by lazy {
		DeleteProjectUseCase(projectRepository)
	}

	private val projectRepository: ProjectsRepository by lazy {
		ProjectRepositoryImpl(projectRemoteDataSource)
	}

	private val projectRemoteDataSource: ProjectRemoteDataSource by lazy {
		ProjectsRemoteDataSourceImpl(RequestManagerContainer.projectApi)
	}

/*	private val projectRemoteDataSource: ProjectRemoteDataSource by lazy {
		ProjectsLocalDataSourceImpl(sharedPreferences)
	}*/


	private val sharedPreferences: SharedPreferences by lazy {
		context.getSharedPreferences("SEEDY_FIUBA", MODE_PRIVATE)
	}

	fun init(context: Context) {
		ProjectsContainer.context = context
	}
}
