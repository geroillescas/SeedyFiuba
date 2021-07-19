package com.fiuba.seedyfiuba.notifications

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.chat.ChatActivity
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity

class PushHandleActivity : BaseActivity() {
	private val pushHandleViewModel by lazy {
		ViewModelProvider(this, PushHandlerViewModelFactory()).get(PushHandleViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setViewState(ViewState.Loading)
		setupObserver()
		val notification = intent.extras?.get(NOTIFICACION) as SeedyFiubaNotification

		if (notification.extra.containsKey("projectId")) {
			val projectId = notification.extra["projectId"]!!.toInt()
			pushHandleViewModel.getProject(projectId)
		}

		if (notification.extra.containsKey("profileId")) {
			val projectId = notification.extra["profileId"]!!.toInt()
			pushHandleViewModel.getProject(projectId)
		}
	}

	private fun setupObserver() {
		pushHandleViewModel.profile.observe(this, {
			goToChat(it)
		})

		pushHandleViewModel.project.observe(this, {
			goToProject(it)
		})
	}

	private fun goToChat(profile: Profile) {
		val intent = Intent(this, ChatActivity::class.java).also {
			it.putExtra(ChatActivity.PROFILE, profile)
		}
		startActivity(intent)
		finish()
	}

	private fun goToProject(project: Project) {
		val intent = Intent(this, ProjectsActivity::class.java).also {
			it.putExtra(ProjectsActivity.PROJECT, project as Parcelable)
		}
		startActivity(intent)
		finish()
	}

	override var layoutResource: Int = R.layout.activity_push_handle

	companion object {
		const val NOTIFICACION = "notification"
	}
}


