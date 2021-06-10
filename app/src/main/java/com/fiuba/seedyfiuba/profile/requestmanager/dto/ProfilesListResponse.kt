package com.fiuba.seedyfiuba.profile.requestmanager.dto

import com.fiuba.seedyfiuba.profile.domain.Profile

data class ProfilesListResponse(
	val totalItems: Int,
	val users: List<Profile>,
	val totalPages: Int,
	val currentPage: Int
)
