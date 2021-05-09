package com.fiuba.seedyfiuba.login.framework.requestmanager

import com.fiuba.seedyfiuba.commons.RestClient
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.HolamundoApi
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.UsersApi

object RequestManagerContainer {
	val usersApi: UsersApi by lazy {
		RestClient.getService(UsersApi::class.java)
	}

	val holamundoApi: HolamundoApi by lazy {
		RestClient.getService(HolamundoApi::class.java)
	}
}

