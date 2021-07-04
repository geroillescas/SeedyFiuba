package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import com.fiuba.seedyfiuba.projects.domain.SearchForm

class SearchProjectsUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(searchForm: SearchForm) = projectsRepository.search(searchForm)
}
