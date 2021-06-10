package com.fiuba.seedyfiuba.profile.domain

import android.os.Parcelable
import com.fiuba.seedyfiuba.login.domain.ProfileType
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Profile(
	val id: Int,
	val name: String,
	val lastName: String,
	val email: String,
	val password: String?,
	val role: ProfileType,
	val description: String?
) : Parcelable
