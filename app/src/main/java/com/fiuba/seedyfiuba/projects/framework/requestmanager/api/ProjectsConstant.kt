package com.fiuba.seedyfiuba.projects.framework.requestmanager.api

object ProjectsConstant {
	//Endpoints
	const val END_POINT_PROJECTS = "/api/project"
	const val END_POINT_REVIEWS = "/reviews"
	const val END_POINT_REVIEWS_ID = "/reviews/{reviewId}"
	const val END_POINT_PROJECT_ID = "/api/project/{id}"
	const val END_POINT_PROJECT_SEARCH = "/api/project/search"
	const val END_POINT_PROJECT_FUND = "/projects/{projectId}/fund"
	const val END_POINT_PROJECT_REVIEW = "/projects/{projectId}/review"
	const val END_POINT_PROJECT_STAGES = "/projects/{projectId}/stages/{stageId}/accept"
	const val END_POINT_PROJECT_CONTRACTS = "/contracts"
	const val END_POINT_WALLET = "/wallet/{userId}"
	const val END_POINT_WALLET_TRANSFER = "/wallet/{userId}/transfer"
}
