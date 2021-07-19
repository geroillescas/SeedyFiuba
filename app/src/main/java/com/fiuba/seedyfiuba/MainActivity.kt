package com.fiuba.seedyfiuba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.home.view.activities.HomeActivity
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.view.activities.LoginActivity
import com.fiuba.seedyfiuba.login.view.activities.OnboardingSetupActivity
import com.fiuba.seedyfiuba.projects.ProjectsContainer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

		val resultLauncher =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
				val setupActivityIntent = Intent(this, OnboardingSetupActivity::class.java)
				startActivity(setupActivityIntent)
				setupFirebaseToken()
				finish()
			}

		setupFirebaseToken()
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
			gotToHome()
		})
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
