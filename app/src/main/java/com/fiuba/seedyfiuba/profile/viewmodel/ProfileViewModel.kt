package com.fiuba.seedyfiuba.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.usecases.*
import com.fiuba.seedyfiuba.projects.domain.EditFormState
import com.fiuba.seedyfiuba.projects.domain.Project
import com.google.firebase.storage.FirebaseStorage


class ProfileViewModel(
	private val getAllProfilesUseCase: GetAllProfilesUseCase,
	private val getProfileUseCase: GetProfileUseCase,
	private val saveProfileUseCase: SaveProfileUseCase,
	private val getProfilesUseCase: GetProfilesUseCase
) : BaseViewModel() {
	private val _profile = MutableLiveData<Profile>()
	val profileLiveData: LiveData<Profile> = _profile

	private val _profileList = MutableLiveData<List<Profile>>()
	val profileListLiveData: LiveData<List<Profile>> = _profileList

	private var page = 0
	private var size = 10
	private var max = 0
	private var fetched = 0

	private val _updated = MutableLiveData<Boolean>()
	val updated: LiveData<Boolean> = _updated

	private val _mShowLoadingSpinner = MutableLiveData<Boolean>()
	val showLoadingSpinner: LiveData<Boolean> = _mShowLoadingSpinner

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

	fun getListProfilesBy(profileType: ProfileType) {
		if (fetched <= max) {
			launch {
				when (val result = getProfilesUseCase.invoke(profileType, size, page)) {
					is Result.Success -> {
						page++
						fetched += result.data.users.size
						max = result.data.totalItems
						_profileList.value?.toMutableList()?.let {
							it.addAll(result.data.users)
							_profileList.postValue(it)
						} ?: run {
							_profileList.postValue(result.data.users)
						}
					}
					is Result.Error -> {
					}
				}
			}
		}
	}

	fun saveProfile(description: String) {
		launch {
			val newProfile = profileLiveData.value?.copy(description = description)
			newProfile?.let { saveProfileUseCase.invoke(it) }
		}
	}

	fun getProfile() {
		launch {
			val userId: Int = AuthenticationManager.userId
			when(val result = getProfileUseCase.invoke(userId)){
				is Result.Success -> {
					_profile.postValue(result.data)
				}

				is Result.Error -> {
					_error.postValue(true)
				}
			}
		}
	}
}
