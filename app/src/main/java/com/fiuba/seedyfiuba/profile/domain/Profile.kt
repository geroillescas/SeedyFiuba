package com.fiuba.seedyfiuba.profile.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Profile(val name: String) : Serializable, Parcelable {
	companion object {
		fun newInstance(): Profile {
			return Profile("")
		}
	}
}

