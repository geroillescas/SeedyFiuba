package com.fiuba.seedyfiuba.funds.data.repositories

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.funds.domains.ContractsListResponse
import java.math.BigDecimal

interface FundRepository{
	suspend fun getAllFunds(funderId: Int): Result<ContractsListResponse>
	suspend fun getPaginatedFunds( size: Int, page: Int, funderId: Int): Result<ContractsListResponse>
	suspend fun transferFunds(userId: Int, wallet: String, amount: BigDecimal): Result<Unit>
}