package com.fiuba.seedyfiuba.projects.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState


class ProjectsActivity : BaseActivity(), FragmentResultListener {
	private lateinit var navController: NavController
	override var layoutResource: Int = R.layout.activity_projects

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setActionBarMode(ActionBarMode.Back)
		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		navController = navHostFragment.navController

		supportFragmentManager.setFragmentResultListener("requestKey", this, this)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
	}

	override fun onBackPressed() {
		if (supportFragmentManager.backStackEntryCount > 0) {
			supportFragmentManager.popBackStack()
		} else {
			super.onBackPressed();
		}
	}

	companion object {
		const val FR_SHOW_LOADING = "FR_SHOW_LOADING"
		const val FR_SHOW_ERROR = "FR_SHOW_ERROR"
		const val BUNDLE_KEY = "BUNDLE_KEY"
	}

	override fun onFragmentResult(requestKey: String, result: Bundle) {
		when (requestKey) {
			FR_SHOW_LOADING -> {
				if (result.getBoolean(BUNDLE_KEY)) {
					setViewState(ViewState.Loading)
				} else {
					setViewState(ViewState.Success)
				}
			}
		}
	}
}

