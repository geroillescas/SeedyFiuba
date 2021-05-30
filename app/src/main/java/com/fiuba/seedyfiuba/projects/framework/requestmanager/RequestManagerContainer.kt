package com.fiuba.seedyfiuba.projects.framework.requestmanager

import com.fiuba.seedyfiuba.commons.BASE_URL_USER
import com.fiuba.seedyfiuba.commons.RestClient
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.UsersApi
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi

object RequestManagerContainer {
	val projectApi: ProjectApi by lazy {
		RestClient.getService(ProjectApi::class.java, BASE_URL_USER)
	}

	val usersApi: UsersApi by lazy {
		RestClient.getService(UsersApi::class.java, BASE_URL_USER)
	}
}

