package com.fiuba.seedyfiuba.chat.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.chat.ChatContainer
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.RemoteBaseDataSource
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.usecases.GetProjectUseCase
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * ViewModel provider factory to instantiate ProjectsViewModel.
 * Required given ProjectsViewModel has a non-empty constructor
 */
class ChatViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return modelClass.getConstructor(
			SendNotificationUseCase::class.java
		).newInstance(
			ChatContainer.sendNotificationUseCase
		)
	}
}

class SendNotificationUseCase(private val chatRepository: ChatRepository) {
	suspend fun invoke(profile: Profile, title: String, body: String) =
		chatRepository.sendNotification(profile, title, body)
}

interface ChatRepository {
	suspend fun sendNotification(profile: Profile, title: String, body: String): Result<Unit>
}

class ChatRepositoryImpl(private val chatDataSource: ChatDataSource) : ChatRepository {
	override suspend fun sendNotification(
		profile: Profile,
		title: String,
		body: String
	): Result<Unit> {
		val chatNotificationDTO = ChatNotificationDTO(
			title,
			body
		)

		val chatDataDTO = ChatDataDTO(
			AuthenticationManager.userId
		)

		val notificationDTO = NotificationDTO(
			profile.firebaseToken!!,
			chatNotificationDTO,
			chatDataDTO
		)
		return chatDataSource.sendNotification(notificationDTO)
	}

}

interface ChatDataSource {
	suspend fun sendNotification(notificationDTO: NotificationDTO): Result<Unit>
}

class ChatDataSourceImpl(private val chatApi: ChatApi) : ChatDataSource, RemoteBaseDataSource() {
	override suspend fun sendNotification(notificationDTO: NotificationDTO): Result<Unit> {
		return getResult {
			chatApi.sendNotification(
				contentType = "application/json",
				authorisation = "key=$KEY_FIREBASE",
				notificationDTO = notificationDTO
			)
		}
	}

	companion object {
		const val KEY_FIREBASE =
			"AAAAu1VBsK8:APA91bEZBAMrqldaQkhMOO1I2FHfGYw0Wgn3CDchmgURXtWyKOco5mRlgtlZ0-kNeOw_qfUQyTGoWeqgobVgAJHJ_79czAig84Ripb9kJkutAK7qGO67Jfj7acp0mHhYu2leRIQ8eu7Y"
	}

}

interface ChatApi {
	@POST("/fcm/send")
	suspend fun sendNotification(
		@Header("Content-Type") contentType: String,
		@Header("Authorization") authorisation: String,
		@Body notificationDTO: NotificationDTO
	): Response<Unit>
}

data class NotificationDTO(
	@SerializedName("to")
	val tokenToSend: String,
	val notification: ChatNotificationDTO,
	val data: ChatDataDTO
)

data class ChatNotificationDTO(
	val title: String,
	val body: String
)

data class ChatDataDTO(
	val profileId: Int
)
