package com.fiuba.seedyfiuba.commons

import com.fiuba.seedyfiuba.chat.model.ChatApi
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileApi
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.MiddleApi
import com.fiuba.seedyfiuba.projects.framework.requestmanager.api.ProjectApi

object RequestManagerContainer {
	val middleApi: MiddleApi  by lazy {
		RestClient.getService(
			serviceClass = MiddleApi::class.java,
			url = BASE_URL_MIDDLE,
			headers = RestClient.getAuthHeaders()
		)
	}

    val profileApi: ProfileApi by lazy {
		RestClient.getService(
			serviceClass = ProfileApi::class.java,
			url = BASE_URL_USER,
			headers = RestClient.getAuthHeaders()
		)
	}

	val projectApi: ProjectApi by lazy {
		RestClient.getService(
			serviceClass = ProjectApi::class.java,
			url = BASE_URL_PROJECTS,
			headers = RestClient.getAuthHeaders()
		)
	}

	val chatApi: ChatApi by lazy {
		RestClient.getService(
			serviceClass = ChatApi::class.java,
			url = BASE_URL_FIREBASE
		)
	}
}

