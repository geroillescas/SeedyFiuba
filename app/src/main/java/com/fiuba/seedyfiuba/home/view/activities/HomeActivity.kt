package com.fiuba.seedyfiuba.home.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModel
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModelFactory
import com.fiuba.seedyfiuba.projects.view.activities.ReviewerConditionsActivity

class HomeActivity : BaseActivity() {

	private lateinit var resultLauncherPick: ActivityResultLauncher<Intent>
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

		resultLauncherPick =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				if (result.resultCode == Activity.RESULT_OK) {
					val list = result.data?.getBooleanExtra(ReviewerConditionsActivity.EXTRA_RESULT, false)
				}
			}
		findViewById<Button>(R.id.helper).setOnClickListener {
			resultLauncherPick.launch(Intent(this, ReviewerConditionsActivity::class.java))
			overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
		}

	}

	private fun setUpView() {

	}
}
