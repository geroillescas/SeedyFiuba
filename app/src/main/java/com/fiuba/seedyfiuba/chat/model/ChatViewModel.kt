package com.fiuba.seedyfiuba.chat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.commons.StringConstants
import com.fiuba.seedyfiuba.login.domain.User
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.usecases.GetProfileUseCase
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.GetProjectUseCase

class ChatViewModel(
	private val sendNotificationUseCase: SendNotificationUseCase
) : BaseViewModel() {
	private lateinit var profile: Profile
	private lateinit var user: User

	fun setup(profileToChat: Profile, userSender: User) {
		profile = profileToChat
		user = userSender
	}

	fun sendMessageNotification(message: String){
		launch {
			val title = "${user.name} te envió un mensaje"
			sendNotificationUseCase.invoke(profile, title, message)
		}
	}

	fun sendImageNotification(){
		launch {
			val title = "${user.name} te envió una imagen"
			sendNotificationUseCase.invoke(profile, title, StringConstants.EMPTY_STRING)
		}
	}
}
