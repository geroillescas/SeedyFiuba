package com.fiuba.seedyfiuba.login.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Data validation state of the login form.
 */
@Parcelize
data class RegisterForm(
	val name: String,
	val lastName: String,
	val email: String,
	val password: String,
	@SerializedName("role")
	val profileType: String
) : Parcelable

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

