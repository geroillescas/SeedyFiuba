package com.fiuba.seedyfiuba.funds.usecases

import com.fiuba.seedyfiuba.funds.data.repositories.FundRepository

class GetFundsUseCase(private val fundRepository: FundRepository) {
	suspend fun invoke(size: Int, page: Int, funderId: Int) = fundRepository.getPaginatedFunds(size, page, funderId)
}