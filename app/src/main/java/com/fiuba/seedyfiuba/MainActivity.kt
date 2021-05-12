package com.fiuba.seedyfiuba

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.view.activities.HolaMundoActivity
import com.fiuba.seedyfiuba.login.view.activities.LoginActivity
import com.fiuba.seedyfiuba.login.view.activities.OnboardingSetupActivity

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		LoginContainer.init(this.applicationContext)
		Handler(Looper.getMainLooper()).postDelayed({
			val intent = Intent(this, LoginActivity::class.java)
			startActivityForResult(intent, RC_LOGIN)
		}, 2000)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RC_LOGIN) {
			val intent = Intent(this, OnboardingSetupActivity::class.java)
			startActivity(intent)
			finish()
		}
	}

	companion object {
		const val RC_LOGIN = 10
		const val RC_LOGIN_EXTRA = "RC_LOGIN_EXTRA"
	}
}
