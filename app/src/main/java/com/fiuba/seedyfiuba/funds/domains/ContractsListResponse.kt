package com.fiuba.seedyfiuba.funds.domains

data class ContractsListResponse(
	val totalItems: Int,
	val results: List<ContractResponse>,
	val totalPages: Int,
	val currentPage: Int
)