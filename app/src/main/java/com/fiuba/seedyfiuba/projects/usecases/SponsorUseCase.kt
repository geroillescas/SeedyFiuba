package com.fiuba.seedyfiuba.projects.usecases

import com.fiuba.seedyfiuba.projects.data.repositories.ProjectsRepository
import java.math.BigDecimal

class SponsorUseCase(private val projectsRepository: ProjectsRepository) {
	suspend fun invoke(amount: BigDecimal, projectId: Int) = projectsRepository.sponsor(amount, projectId)
}
