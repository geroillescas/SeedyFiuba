package com.fiuba.seedyfiuba.profile.view.activities

import android.content.Intent
import android.os.Bundle
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.chat.ChatActivity
import com.fiuba.seedyfiuba.chat.ChatEntryPointActivity
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ContactActivity : ProfileListActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setTitle(R.string.title_activity_contact)
	}

	override fun onProfileClicked(profile: Profile, position: Int) {
		val intent = Intent(this, ChatEntryPointActivity::class.java).also {
			it.putExtra(ChatActivity.PROFILE, profile)
		}

		startActivity(intent)
	}

	override fun fetchMoreInfo() {
		when (AuthenticationManager.session?.user?.profileType) {
			ProfileType.EMPRENDEDOR -> {
				profileListViewModel.getListProfilesBy(ProfileType.PATROCINADOR)
			}

			ProfileType.PATROCINADOR -> {
				profileListViewModel.getListProfilesBy(ProfileType.EMPRENDEDOR)
			}

			else ->
				super.fetchMoreInfo()

		}
	}
}
