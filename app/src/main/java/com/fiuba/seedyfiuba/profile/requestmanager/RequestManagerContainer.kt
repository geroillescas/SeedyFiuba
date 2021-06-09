package com.fiuba.seedyfiuba.profile.requestmanager

import com.fiuba.seedyfiuba.commons.BASE_URL_USER
import com.fiuba.seedyfiuba.commons.RestClient
import com.fiuba.seedyfiuba.profile.requestmanager.api.ProfileApi

object RequestManagerContainer {
	val profileApi: ProfileApi by lazy {
		RestClient.getService(
			serviceClass = ProfileApi::class.java,
			url = BASE_URL_USER,
			headers = RestClient.getAuthHeaders()
		)
	}
}

