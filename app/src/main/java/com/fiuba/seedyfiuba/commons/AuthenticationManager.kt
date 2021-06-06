package com.fiuba.seedyfiuba.commons

import com.fiuba.seedyfiuba.login.domain.Session

object AuthenticationManager {
	var session: Session? = null

	fun initialize(session: Session){
		this.session = session
	}
}
