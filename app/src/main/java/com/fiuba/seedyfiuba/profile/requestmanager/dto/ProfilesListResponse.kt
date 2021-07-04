package com.fiuba.seedyfiuba.profile.requestmanager.dto

import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.domain.Project
import com.google.gson.annotations.SerializedName

data class ProfilesListResponse(
	val totalItems: Int,
	val users: List<Profile>,
	val totalPages: Int,
	val currentPage: Int
)


data class ReviewerResponse(
	val project: Project,
	val reviewer: Review
)

data class Review(
	@SerializedName("reviewerId")
	val reviewerID: Long,
	@SerializedName("projectId")
	val projectID: Long,
	val id: Long,
	val status: String
)


data class ReviewerPostRequest(
	@SerializedName("reviewerId")
	val reviewerID: Long,
	@SerializedName("projectId")
	val projectID: Long
)

data class ReviewerPutRequest(
	val status: String
)
