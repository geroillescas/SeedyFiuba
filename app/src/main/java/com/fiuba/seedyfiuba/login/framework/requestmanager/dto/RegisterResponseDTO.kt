package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterResponseDTO(
	@SerializedName("id")
	val userId: String,
	val name: String,
	val lastName: String,
	val email: String,
	@SerializedName("role")
	val profileType: String
) : Parcelable
