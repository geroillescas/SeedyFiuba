package com.fiuba.seedyfiuba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fiuba.seedyfiuba.chat.ChatContainer
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.home.view.activities.HomeActivity
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.view.activities.LoginActivity
import com.fiuba.seedyfiuba.login.view.activities.OnboardingSetupActivity
import com.fiuba.seedyfiuba.profile.ProfileContainer
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.view.activities.ContactActivity
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

	private val session = MutableLiveData<Session?>()
	private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		FirebaseApp.initializeApp(this.applicationContext)
		LoginContainer.init(this.applicationContext)
		ProjectsContainer.init(this.applicationContext)
		ProfileContainer.init(this.applicationContext)
		ChatContainer.init(this.applicationContext)

		val resultLauncher =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
				val setupActivityIntent = Intent(this, OnboardingSetupActivity::class.java)
				startActivity(setupActivityIntent)
				setupFirebaseToken()
				finish()
			}

		session.observe(this, {
			runOnUiThread {
				it?.let {
					AuthenticationManager.initialize(it)
					setupFirebaseToken()
				} ?: run {
					val intent = Intent(this, LoginActivity::class.java)
					resultLauncher.launch(intent)
				}
			}
		})
	}

	private fun setupFirebaseToken() {
		FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
			if (!task.isSuccessful) {
				Log.w(TAG, "Fetching FCM registration token failed", task.exception)
				return@OnCompleteListener
			}

			// Get new FCM registration token
			val token = task.result
			coroutineScope.launch {
				ProfileContainer.saveProfileTokenUpdateDTOUseCase.invoke(token)
			}
			gotToHome()
		})
	}

	private fun loginWithFirebase() {
		/*AuthenticationManager.session?.user?.email?.let {

			if(Firebase.auth.currentUser == null){
				val token = AuthenticationManager.session?.user?.password!!
				FirebaseAuth.getInstance().signInWithEmailAndPassword(it, token).addOnCompleteListener(this) { task ->
					if (task.isSuccessful) {
						// Sign in success, update UI with the signed-in user's information
						Log.d(ContactActivity::class.qualifiedName, "createUserWithEmail:success")
						setupFirebaseToken()
					} else {
						// If sign in fails, display a message to the user.
						Log.w(
							ContactActivity::class.qualifiedName,
							"createUserWithEmail:failure",
							task.exception
						)
						FirebaseAuth.getInstance().createUserWithEmailAndPassword(
							it, token
						).addOnCompleteListener {
							setupFirebaseToken()
						}
					}
				}
			}
			else{
				setupFirebaseToken()
			}
		}*/
	}

	private fun gotToHome(){
		val intent = Intent(this, HomeActivity::class.java)
		startActivity(intent)
		finish()
	}

	override fun onResume() {
		super.onResume()


		coroutineScope.launch {
			session.postValue(LoginContainer.getSessionUseCase.invoke())
		}
	}

	companion object {
		const val TAG = "MainActivity"
	}
}
