package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SessionDTO(
	val token: String,
	val user: UserDTO
) : Parcelable
