package com.fiuba.seedyfiuba.login.framework.requestmanager

import com.fiuba.seedyfiuba.commons.RestClient
import com.fiuba.seedyfiuba.login.framework.requestmanager.api.HolamundoApi

object RequestManagerContainer {
    val holamundoApi: HolamundoApi by lazy {
		RestClient.getService(HolamundoApi::class.java)
    }
}

