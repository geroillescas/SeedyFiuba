package com.fiuba.seedyfiuba.projects.framework.requestmanager.api

object ProjectsConstant {
	//Endpoints
	const val END_POINT_PROJECTS = "/projects"
	const val END_POINT_REVIEWS = "/reviews"
	const val END_POINT_REVIEWS_ID = "/reviews/{reviewId}"
	const val END_POINT_PROJECT_ID = "/projects/{id}"
	const val END_POINT_PROJECT_SEARCH = "/projects/search"
	const val END_POINT_PROJECT_FUND = "/projects/{projectId}/fund"
	const val END_POINT_PROJECT_REVIEW = "/projects/{projectId}/review"
	const val END_POINT_PROJECT_STAGES = "/projects/{projectId}/stages/{stageId}/accept"
	const val END_POINT_PROJECT_CONTRACTS = "/contracts"
	const val END_POINT_WALLET = "/wallet/{userId}"
	const val END_POINT_WALLET_TRANSFER = "/wallet/{userId}/transfer"
}
