package com.fiuba.seedyfiuba.login.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
	val token: String,
	val user: RegisterResponseDTO
) : Parcelable
