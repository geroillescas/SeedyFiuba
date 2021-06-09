package com.fiuba.seedyfiuba.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.usecases.GetAllProfilesUseCase
import com.fiuba.seedyfiuba.profile.usecases.GetProfileUseCase
import com.fiuba.seedyfiuba.profile.usecases.SaveProfileUseCase
import com.fiuba.seedyfiuba.profile.usecases.UpdateProfileUseCase
import com.fiuba.seedyfiuba.projects.domain.EditFormState
import com.fiuba.seedyfiuba.projects.domain.Project
import com.google.firebase.storage.FirebaseStorage


class ProfileViewModel(
	private val getAllProfilesUseCase: GetAllProfilesUseCase,
	private val getProfileUseCase: GetProfileUseCase,
	private val saveProfileUseCase: SaveProfileUseCase,
	private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {
	private val _profile = MutableLiveData<Profile>()
	val profileLiveData: LiveData<Profile> = _profile

	private val _profileList = MutableLiveData<List<Profile>>()
	val profileListLiveData: LiveData<List<Profile>> = _profileList

	fun getListProfiles() {
		launch {
			when(val result = getAllProfilesUseCase.invoke()){
				is Result.Success -> {
					_profileList.postValue(result.data)
				}

				is Result.Error -> {
					_error.postValue(true)
				}
			}
		}
	}
}
