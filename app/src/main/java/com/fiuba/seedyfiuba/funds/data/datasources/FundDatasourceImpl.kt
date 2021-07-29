package com.fiuba.seedyfiuba.funds.data.datasources

import android.content.Context
import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.funds.domains.ContractsListResponse
import com.fiuba.seedyfiuba.funds.domains.TransferRequest
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.MiddleApi

class FundDatasourceImpl(private val fundApi: MiddleApi,
	context: Context
): FundDatasource, RemoteBaseDataSource(context) {
	override suspend fun getAllFunds(funderId: Int): Result<ContractsListResponse> {
		return getResult {
			fundApi.getFunds(funderId)
		}
	}

	override suspend fun getPaginatedFunds(size: Int, page: Int, funderId: Int): Result<ContractsListResponse> {
		return getResult {
			fundApi.getFunds(size, page, funderId)
		}
	}

	override suspend fun transferFund(userId: Int, transferRequest: TransferRequest): Result<Unit> {
		return getResultUnit {
			fundApi.transferFund(userId, transferRequest)
		}
	}
}