package com.fiuba.seedyfiuba.profile.data.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.fragments.SearchForm

interface ProfileRemoteDataSource {
	suspend fun getProfile(): Result<Profile>
	suspend fun getAllProfile(): Result<List<Profile>>
	suspend fun saveProfile(profile: Profile): Result<Profile>
}
