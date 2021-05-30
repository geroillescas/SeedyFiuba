package com.fiuba.seedyfiuba.home.view.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModel
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModelFactory

class HomeActivity : BaseActivity() {

	private lateinit var navController: NavController
	private val homeViewModel by lazy {
		ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)
	}

	override var layoutResource: Int = R.layout.activity_home

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setUpView()
		setActionBarMode(ActionBarMode.Home)


		homeViewModel.getLocalSession()

	}

	private fun setUpView() {

	}
}
