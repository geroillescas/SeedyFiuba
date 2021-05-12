package com.fiuba.seedyfiuba.login.framework.requestmanager.datasources

import android.content.SharedPreferences
import com.fiuba.seedyfiuba.login.data.datasources.LoginLocalDataSource
import com.fiuba.seedyfiuba.login.domain.Session
import com.google.gson.Gson

class LoginLocalDataSourceImpl(private val sharedPreferences: SharedPreferences) :
	LoginLocalDataSource {
	override suspend fun saveSession(session: Session) {
		val serializedSession = Gson().toJson(session).toString()
		sharedPreferences.edit().putString(KEY, serializedSession).apply()
	}

	override suspend fun getSession(): Session {
		val serializedSession = sharedPreferences.getString(KEY, "")
		return Gson().fromJson(serializedSession, Session::class.java)
	}

	companion object {
		const val KEY = "SESSION"
	}
}
