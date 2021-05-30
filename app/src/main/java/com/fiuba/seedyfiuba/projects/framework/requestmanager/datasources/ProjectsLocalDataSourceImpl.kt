package com.fiuba.seedyfiuba.projects.framework.requestmanager.datasources

import android.content.SharedPreferences
import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.data.datasources.ProjectRemoteDataSource
import com.fiuba.seedyfiuba.projects.domain.Project
import com.google.gson.Gson

class ProjectsLocalDataSourceImpl(private val sharedPreferences: SharedPreferences) :
	RemoteBaseDataSource(),
	ProjectRemoteDataSource {
	override suspend fun updateProject(project: Project): Result<Boolean> {
		val list = getProjectsList().toMutableList()
		val index = list.indexOfFirst { it.id == project.id }
		if (index >= 0) {
			list.removeAt(index)
			list.add(index, project)
		}
		saveProjectsList(list)
		return Result.Success(true)
	}

	override suspend fun saveProject(project: Project): Result<Boolean> {
		val list = getProjectsList().toMutableList().apply {
			add(project.copy(id = size))
		}
		saveProjectsList(list)
		return Result.Success(true)
	}

	override suspend fun deleteProject(project: Project): Result<Boolean> {
		val filteredList = getProjectsList().filter { it.id != project.id }
		saveProjectsList(filteredList)
		return Result.Success(true)
	}

	override suspend fun getProjects(): Result<List<Project>> {
		return Result.Success(getProjectsList())
	}

	private fun getProjectsList(): List<Project> {
		val serializedList = sharedPreferences.getString(KEY, "")
		serializedList?.let {
			if (it.isNotEmpty()) {
				return Gson()
					.fromJson(serializedList, Array<Project>::class.java).toList()
			}
		}
		return arrayListOf()
	}

	private fun saveProjectsList(projects: List<Project>) {
		val serializedProject = Gson().toJson(projects).toString()
		sharedPreferences.edit().putString(KEY, serializedProject).apply()
	}


	companion object {
		const val KEY = "PROJECTS"
	}

}