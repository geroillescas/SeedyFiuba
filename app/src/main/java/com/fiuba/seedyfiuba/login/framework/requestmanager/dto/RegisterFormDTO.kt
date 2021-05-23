package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterFormDTO(
	val name: String,
	val lastName: String,
	val email: String,
	val password: String,
	val role: String
) : Parcelable
