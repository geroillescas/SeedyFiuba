package com.fiuba.seedyfiuba.login.framework.requestmanager.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginGoogleRequestDTO(
	@SerializedName("idToken")
	val idToken: String
) : Parcelable
