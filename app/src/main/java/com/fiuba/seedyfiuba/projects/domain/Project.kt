package com.fiuba.seedyfiuba.projects.domain

import android.os.Parcelable
import com.fiuba.seedyfiuba.login.domain.ProjectType
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

@Parcelize
data class Project(
	val id: Int = 0,
	val title: String,
	val description: String,
	val type: ProjectType,
	val status: ProjectStatus,
	val location: String,
	val hashtags: List<String>,
	val mediaUrls: MutableList<String>,
	val amount: BigDecimal,
	val date: Date
) : Parcelable, Serializable {
	companion object {
		fun newInstance(): Project {
			return Project(
				0,
				title = "",
				description = "",
				type = ProjectType.ACADEMIC,
				status = ProjectStatus.COMPLETED,
				location = "",
				hashtags = listOf(),
				mediaUrls = mutableListOf(),
				amount = BigDecimal.ZERO,
				date = Date()
			)
		}
	}
}
