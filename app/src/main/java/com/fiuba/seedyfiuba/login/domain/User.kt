package com.fiuba.seedyfiuba.login.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
	val userId: Int,
	val name: String,
	val lastName: String,
	val email: String,
	val password: String,
	val profileType: ProfileType
) : Parcelable
