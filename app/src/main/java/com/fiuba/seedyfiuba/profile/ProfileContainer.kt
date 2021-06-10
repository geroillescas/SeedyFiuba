package com.fiuba.seedyfiuba.profile

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.fiuba.seedyfiuba.profile.data.datasources.ProfileRemoteDataSource
import com.fiuba.seedyfiuba.profile.data.repositories.ProfileRepository
import com.fiuba.seedyfiuba.profile.data.repositories.ProfileRepositoryImpl
import com.fiuba.seedyfiuba.profile.requestmanager.RequestManagerContainer
import com.fiuba.seedyfiuba.profile.requestmanager.datasources.ProfileRemoteDataSourceImpl
import com.fiuba.seedyfiuba.profile.usecases.GetAllProfilesUseCase
import com.fiuba.seedyfiuba.profile.usecases.GetProfileUseCase
import com.fiuba.seedyfiuba.profile.usecases.SaveProfileUseCase

object ProfileContainer {

	private lateinit var context: Context

	//Use Cases
	val getProfileUseCase: GetProfileUseCase by lazy {
		GetProfileUseCase(profileRepository)
	}

	val getAllProfilesUseCase: GetAllProfilesUseCase by lazy {
		GetAllProfilesUseCase(profileRepository)
	}

	val saveProfileUseCase: SaveProfileUseCase by lazy {
		SaveProfileUseCase(profileRepository)
	}

	private val profileRepository: ProfileRepository by lazy {
		ProfileRepositoryImpl(profileRemoteDataSource)
	}

	private val profileRemoteDataSource: ProfileRemoteDataSource by lazy {
		ProfileRemoteDataSourceImpl(RequestManagerContainer.profileApi)
	}

	private val sharedPreferences: SharedPreferences by lazy {
		context.getSharedPreferences("SEEDY_FIUBA", MODE_PRIVATE)
	}

	fun init(context: Context) {
		ProfileContainer.context = context
	}
}
