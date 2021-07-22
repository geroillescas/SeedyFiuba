package com.fiuba.seedyfiuba.notifications

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeedyFiubaNotification(
	val title: String? = null,
	val body: String? = null,
	val imageUrl: Uri? = null,
	val extra: Map<String, String> = mapOf()
) : Parcelable
