package com.fiuba.seedyfiuba.home.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModel
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModelFactory
import com.fiuba.seedyfiuba.login.viewmodel.LoginViewModel
import com.fiuba.seedyfiuba.login.viewmodel.LoginViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

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
