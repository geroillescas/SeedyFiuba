package com.fiuba.seedyfiuba.projects.domain

import android.location.Location
import com.fiuba.seedyfiuba.login.domain.ProjectType
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus

data class SearchForm(
    val hashtag: String? = null,
    val projectType: ProjectType? = null,
    val projectStatus: ProjectStatus? = null,
    val location: LocationProject? = null
)
