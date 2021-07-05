package com.fiuba.seedyfiuba.profile.requestmanager.dto

import android.os.Parcelable
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.domain.Project
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ProfilesListResponse(
	val totalItems: Int,
	val users: List<Profile>,
	val totalPages: Int,
	val currentPage: Int
)
data class ReviewerListResponse(
	val size: Int,
	val results: List<ReviewerResponse>
)

data class ReviewerResponse(
	val project: Project?,
	val review: Review
)

@Parcelize
data class Review(
	@SerializedName("reviewerId")
	val reviewerID: Int,
	@SerializedName("projectId")
	val projectID: Int,
	val id: Int,
	val status: String
) : Parcelable

data class ReviewerPostRequest(
	@SerializedName("reviewerId")
	val reviewerID: Int,
	@SerializedName("projectId")
	val projectID: Int
)

data class ReviewerPutRequest(
	val status: String
)
