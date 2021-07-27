package com.fiuba.seedyfiuba.funds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.RequestManagerContainer
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.MiddleApi
import com.fiuba.seedyfiuba.projects.usecases.GetProjectUseCase
import java.math.BigDecimal


/**
 * This factory should create the required viewModel by type for the view
 */
class FundsHistoryViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(FundsHistoryViewModel::class.java)) {
			return modelClass.getConstructor(
				GetFundsUseCase::class.java,
				GetProjectUseCase::class.java
			).newInstance(
				FundsContainer.getFundsUseCase,
				ProjectsContainer.getProjectUseCase
			)
		}
		throw IllegalStateException("ViewModel must extend ${modelClass.name}")
	}
}

class GetFundsUseCase(private val fundRepository: FundRepository) {
	suspend fun invoke(size: Int, page: Int, funderId: Int) = fundRepository.getPaginatedFunds(size, page, funderId)
}

class TransferUserCase(private val fundRepository: FundRepository) {
	suspend fun invoke(userId: Int, wallet: String, amount: BigDecimal) = fundRepository.transferFunds(userId, wallet, amount)
}

interface FundRepository{
	suspend fun getAllFunds(funderId: Int):  Result<ContractsListResponse>
	suspend fun getPaginatedFunds( size: Int, page: Int, funderId: Int):  Result<ContractsListResponse>
	suspend fun transferFunds(userId: Int, wallet: String, amount: BigDecimal): Result<Unit>
}

interface FundDatasource{
	suspend fun getAllFunds(funderId: Int): Result<ContractsListResponse>
	suspend fun getPaginatedFunds( size: Int, page: Int, funderId: Int): Result<ContractsListResponse>
	suspend fun transferFund(userId: Int, transferRequest: TransferRequest): Result<Unit>
}

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

data class TransferRequest(val destinationAddress: String, val amount: BigDecimal)

class FundDatasourceImpl(private val fundApi: MiddleApi): FundDatasource, RemoteBaseDataSource() {
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

object FundsContainer {
	val getFundsUseCase : GetFundsUseCase by lazy {
		GetFundsUseCase(fundRepository)
	}

	val transferUserCase : TransferUserCase by lazy {
		TransferUserCase(fundRepository)
	}

	private val fundRepository : FundRepository by lazy {
		FundRepositoryImpl(fundDatasource)
	}

	private val fundDatasource: FundDatasource by lazy {
		FundDatasourceImpl(RequestManagerContainer.middleApi)
	}
}

data class ContractsListResponse(
	val totalItems: Int,
	val results: List<ContractResponse>,
	val totalPages: Int,
	val currentPage: Int
)

data class ContractResponse(
	val project: Project,
	val contract: Contract
)
data class Contract(
	val funder: Int,
	val currentFundedAmount: BigDecimal,
	val projectId: Int
)
