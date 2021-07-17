package com.fiuba.seedyfiuba.profile.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.chat.ChatActivity
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
		val intent = Intent(this, ChatActivity::class.java).also {
			it.putExtra(ChatActivity.PROFILE, profile)
		}

		setViewState(ViewState.Loading)
		AuthenticationManager.session?.user?.email?.let {
			if(Firebase.auth.currentUser == null){
				val token = AuthenticationManager.session?.token!!
				FirebaseAuth.getInstance().createUserWithEmailAndPassword(it, token).addOnCompleteListener(this) { task ->
					if (task.isSuccessful) {
						// Sign in success, update UI with the signed-in user's information
						Log.d(ContactActivity::class.qualifiedName, "createUserWithEmail:success")
						runOnUiThread {
							setViewState(ViewState.Initial)
							startActivity(intent)
						}
					} else {
						// If sign in fails, display a message to the user.
						Log.w(
							ContactActivity::class.qualifiedName,
							"createUserWithEmail:failure",
							task.exception
						)
						runOnUiThread {
							setViewState(ViewState.Initial)
							startActivity(intent)
						}
					}
				}
			}
			else{
				startActivity(intent)
			}
		}
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
