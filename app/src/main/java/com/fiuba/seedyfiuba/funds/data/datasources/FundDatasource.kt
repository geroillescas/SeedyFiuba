package com.fiuba.seedyfiuba.funds.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.funds.domains.ContractsListResponse
import com.fiuba.seedyfiuba.funds.domains.TransferRequest

interface FundDatasource{
	suspend fun getAllFunds(funderId: Int): Result<ContractsListResponse>
	suspend fun getPaginatedFunds( size: Int, page: Int, funderId: Int): Result<ContractsListResponse>
	suspend fun transferFund(userId: Int, transferRequest: TransferRequest): Result<Unit>
}