package com.fiuba.seedyfiuba.login.domain

import kotlinx.android.parcel.Parcelize

@Parcelize
data class HolaMundoModel(
	val fullName: String
) : Entity()
