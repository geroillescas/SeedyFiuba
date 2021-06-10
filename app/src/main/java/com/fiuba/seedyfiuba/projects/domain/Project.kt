package com.fiuba.seedyfiuba.projects.domain

import android.os.Parcelable
import com.fiuba.seedyfiuba.login.domain.ProjectType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

@Parcelize
data class Project(
	@SerializedName("_id")
	val id: Int = 0,
	val title: String,
	val description: String,
	@SerializedName("category")
	val type: ProjectType = ProjectType.ACADEMIC,
	val location: LocationProject? = null,
	val hashtags: List<String> = listOf(),
	val mediaUrls: MutableList<String> = mutableListOf(),
	val stages: List<Stages> = listOf(),
	val finishDate: Date = Date()
) : Serializable, Parcelable {
	companion object {
		fun newInstance(): Project {
			return Project(
				0,
				title = "",
				description = "",
				type = ProjectType.ACADEMIC,
				location = LocationProject(),
				hashtags = listOf(),
				mediaUrls = mutableListOf()
				//date = Date()
			)
		}
	}
}

data class ProjectRequestDTO(
	val title: String,
	val description: String,
	val ownerId: Int,
	val category: ProjectType = ProjectType.ACADEMIC,
	val location: LocationProject? = null,
	val hashtags: List<String> = listOf(),
	val mediaUrls: List<String> = listOf(),
	val stages: List<Stages> = listOf(),
	val finishDate: Date = Date()
)

data class ProjectUpdateDTO(
	val title: String,
	val description: String,
	val category: ProjectType = ProjectType.ACADEMIC,
	val location: LocationProject? = null,
	val hashtags: List<String> = listOf(),
	val mediaUrls: List<String> = listOf()
)

data class SearchResponseDTO(
	val size: Int,
	val results: List<Project>
)


@Parcelize
data class Stages(
	val track: String,
	val targetAmount: BigDecimal
) : Parcelable

@Parcelize
data class LocationProject(
	var x: BigDecimal = BigDecimal.ZERO,
	var y: BigDecimal = BigDecimal.ZERO
) : Parcelable
