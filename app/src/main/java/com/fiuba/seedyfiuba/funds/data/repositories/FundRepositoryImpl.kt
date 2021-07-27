package com.fiuba.seedyfiuba.funds.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.funds.data.datasources.FundDatasource
import com.fiuba.seedyfiuba.funds.domains.ContractsListResponse
import com.fiuba.seedyfiuba.funds.domains.TransferRequest
import java.math.BigDecimal

class FundRepositoryImpl(private val fundDatasource: FundDatasource): FundRepository {
	override suspend fun getAllFunds(funderId: Int): Result<ContractsListResponse> {
		return fundDatasource.getAllFunds(funderId)
	}

	override suspend fun getPaginatedFunds(size: Int, page: Int, funderId: Int): Result<ContractsListResponse> {
		return fundDatasource.getPaginatedFunds(size, page, funderId)
	}

	override suspend fun transferFunds(userId: Int, wallet: String, amount: BigDecimal): Result<Unit> {
		val transferRequest = TransferRequest(wallet, amount)
		return fundDatasource.transferFund(userId, transferRequest)
	}
}