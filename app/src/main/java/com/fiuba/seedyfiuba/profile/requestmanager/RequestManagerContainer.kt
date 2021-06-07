package com.fiuba.seedyfiuba.profile.requestmanager

import com.fiuba.seedyfiuba.commons.BASE_URL_PROJECTS
import com.fiuba.seedyfiuba.commons.RestClient
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileApi
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi

object RequestManagerContainer {
	val profileApi: ProfileApi by lazy {
		RestClient.getService(
			serviceClass = ProfileApi::class.java,
			url = BASE_URL_PROJECTS,
			headers = RestClient.getAuthHeaders()
		)
	}
}

