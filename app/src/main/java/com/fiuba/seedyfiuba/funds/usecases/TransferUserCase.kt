package com.fiuba.seedyfiuba.funds.usecases

import com.fiuba.seedyfiuba.funds.data.repositories.FundRepository
import java.math.BigDecimal

class TransferUserCase(private val fundRepository: FundRepository) {
	suspend fun invoke(userId: Int, wallet: String, amount: BigDecimal) = fundRepository.transferFunds(userId, wallet, amount)
}