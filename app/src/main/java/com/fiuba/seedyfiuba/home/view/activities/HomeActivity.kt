package com.fiuba.seedyfiuba.home.view.activities

import android.os.Bundle
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity

class HomeActivity : BaseActivity() {


	override var layoutResource: Int = R.layout.activity_home

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setUpView()
		setActionBarMode(ActionBarMode.Home)

	}


	private fun setUpView() {

	}
}
