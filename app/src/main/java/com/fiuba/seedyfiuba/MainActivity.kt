package com.fiuba.seedyfiuba

import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

	private val session = MutableLiveData<Session?>()
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
				finish()
			}
		session.observe(this, Observer {
			runOnUiThread {
				it?.let {
					AuthenticationManager.initialize(it)
					val intent = Intent(this, HomeActivity::class.java)
					startActivity(intent)
					finish()
				} ?: run {
					val intent = Intent(this, LoginActivity::class.java)
					resultLauncher.launch(intent)
				}
			}
		})
	}

	override fun onResume() {
		super.onResume()

		val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
		coroutineScope.launch {
			session.postValue(LoginContainer.getSessionUseCase.invoke())
		}
	}
}
