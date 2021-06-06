package com.fiuba.seedyfiuba.projects.framework.requestmanager

import com.fiuba.seedyfiuba.commons.BASE_URL_PROJECTS
import com.fiuba.seedyfiuba.commons.RestClient
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi

object RequestManagerContainer {
	val projectApi: ProjectApi by lazy {
		RestClient.getService(
			serviceClass = ProjectApi::class.java,
			url = BASE_URL_PROJECTS,
			headers = RestClient.getAuthHeaders()
		)
	}
}

