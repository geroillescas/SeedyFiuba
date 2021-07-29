package com.fiuba.seedyfiuba.funds

import android.content.Context
import com.fiuba.seedyfiuba.commons.RequestManagerContainer
import com.fiuba.seedyfiuba.funds.data.datasources.FundDatasource
import com.fiuba.seedyfiuba.funds.data.datasources.FundDatasourceImpl
import com.fiuba.seedyfiuba.funds.data.repositories.FundRepository
import com.fiuba.seedyfiuba.funds.data.repositories.FundRepositoryImpl
import com.fiuba.seedyfiuba.funds.usecases.GetFundsUseCase
import com.fiuba.seedyfiuba.funds.usecases.TransferUserCase

object FundsContainer {
	private lateinit var context: Context

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
        FundDatasourceImpl(RequestManagerContainer.middleApi, context)
	}

	fun init(context: Context) {
		FundsContainer.context = context
	}
}