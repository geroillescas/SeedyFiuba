package com.fiuba.seedyfiuba.funds.domains

import java.math.BigDecimal

data class Contract(
	val funder: Int,
	val currentFundedAmount: BigDecimal,
	val projectId: Int
)