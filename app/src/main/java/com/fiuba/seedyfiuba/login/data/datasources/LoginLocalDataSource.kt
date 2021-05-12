package com.fiuba.seedyfiuba.login.data.datasources

import com.fiuba.seedyfiuba.login.domain.Session

interface LoginLocalDataSource {
	suspend fun saveSession(session: Session)
	suspend fun getSession(): Session
}
