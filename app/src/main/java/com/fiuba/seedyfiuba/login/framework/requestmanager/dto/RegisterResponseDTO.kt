package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterResponseDTO(
	@SerializedName("_id")
	val userId: String,
	val name: String,
	val lastName: String,
	val email: String,
	val password: String,
	@SerializedName("role")
	val profileType: String
) : Parcelable
