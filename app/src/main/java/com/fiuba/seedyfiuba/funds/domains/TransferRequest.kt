package com.fiuba.seedyfiuba.funds.domains

import java.math.BigDecimal

data class TransferRequest(val destinationAddress: String, val amount: BigDecimal)