package com.fiuba.seedyfiuba.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.view.activities.ContactActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatEntryPointActivity : BaseActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setInitialViewState(ViewState.Loading)
		loginWithFirebase()
	}

	override var layoutResource: Int = R.layout.activity_chat_entry_point

	private fun loginWithFirebase() {
		Firebase.auth.signOut()
		val user = AuthenticationManager.session!!.user
		FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.email).addOnCompleteListener(this) { task ->
			if (task.isSuccessful) {
				// Sign in success, update UI with the signed-in user's information
				Log.d(ContactActivity::class.qualifiedName, "createUserWithEmail:success")
				goToChat()
			} else {
				// If sign in fails, display a message to the user.
				Log.w(
					ContactActivity::class.qualifiedName,
					"createUserWithEmail:failure",
					task.exception
				)
				goToChat()
			}
		}
	}

	private fun goToChat() {
		val profile = intent.extras?.get(ChatActivity.PROFILE) as Profile
		val newIntent = Intent(this, ChatActivity::class.java).also {
			it.putExtra(ChatActivity.PROFILE, profile)
		}

		runOnUiThread {
			startActivity(newIntent)
			finish()
		}

	}
}
