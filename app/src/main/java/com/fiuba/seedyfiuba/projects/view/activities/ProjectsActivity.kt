package com.fiuba.seedyfiuba.projects.view.activities

import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.fragments.DetailProjectFragment


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

		intent.extras?.get(PROJECT)?.let {
			val bundle = Bundle().apply {
				putParcelable(DetailProjectFragment.ARG_PROJECT, it as Project)
			}
			when (AuthenticationManager.session?.user?.profileType) {
				ProfileType.VEEDOR -> {
					navController.navigate(R.id.reviewerDetailProjectFragment, bundle)
				}

				else -> {
					navController.navigate(R.id.detailProjectFragment, bundle)
				}
			}
		}
	}

	override fun onBackPressed() {
		if (supportFragmentManager.backStackEntryCount > 0) {
			supportFragmentManager.popBackStack()
		} else {
			super.onBackPressed()
		}
	}

	companion object {
		const val FR_SHOW_LOADING = "FR_SHOW_LOADING"
		const val FR_SHOW_ERROR = "FR_SHOW_ERROR"
		const val BUNDLE_KEY = "BUNDLE_KEY"
		const val PROJECT = "project"
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

