package com.fiuba.seedyfiuba.funds.domains

import com.fiuba.seedyfiuba.projects.domain.Project

data class ContractResponse(
	val project: Project,
	val contract: Contract
) {
	fun getSubtitle() : String {
		return "Monto pagado ${contract.currentFundedAmount}"
	}
}