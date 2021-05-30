package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import android.os.Parcelable
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDTO(
	val userId: String?,
	val name: String,
	val lastName: String?,
	val email: String?,
	val password: String?,
	@SerializedName("role")
	val profileType: ProfileType?
) : Parcelable
