package com.fiuba.seedyfiuba.projects.domain

import com.fiuba.seedyfiuba.login.domain.ProjectType

data class SearchForm(
    val hashtag: String? = null,
    val projectType: ProjectType? = null,
    val projectStatus: ProjectStatus? = null,
    val location: LocationProject? = null
)
