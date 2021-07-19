package com.fiuba.seedyfiuba.commons

import com.fiuba.seedyfiuba.login.domain.Session

object AuthenticationManager {
	var session: Session? = null

	val userId: Int
		get() {
			return session?.user?.userId ?: 0
		}

	fun initialize(session: Session){
		this.session = session
	}
}
