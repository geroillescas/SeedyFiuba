package com.fiuba.seedyfiuba.login.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.home.view.activities.HomeActivity
import com.fiuba.seedyfiuba.login.viewmodel.OnbaordingSetupViewModelFactory
import com.fiuba.seedyfiuba.login.viewmodel.OnboardingSetupViewModel

class OnboardingSetupActivity : BaseActivity() {

	private val onboardingSetupViewModel by lazy {
		ViewModelProvider(
			this,
			OnbaordingSetupViewModelFactory()
		).get(OnboardingSetupViewModel::class.java)
	}

	private lateinit var continueButton: Button
	private lateinit var spinnerLocale: Spinner
	private lateinit var spinnerTypeProyect: Spinner

	override var layoutResource: Int = R.layout.activity_onboarding_setup

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setupView()
		setActionBarMode(ActionBarMode.None)
		setupObservers()
		setupSpinner()
		setupButton()
		onboardingSetupViewModel.getSession()
	}

	private fun setupView() {
		spinnerLocale = findViewById(R.id.onboarding_setup_locale)
		spinnerTypeProyect = findViewById(R.id.onboarding_setup_type_proyect)
		continueButton = findViewById(R.id.onboarding_setup_btn)
	}

	private fun setupObservers() {
		onboardingSetupViewModel.showLoading.observe(this, Observer {
			if (it) {
				setViewState(ViewState.Loading)
			} else {
				setViewState(ViewState.Success)
			}
		})
		onboardingSetupViewModel.finished.observe(this, Observer {
			val intent = Intent(this, HomeActivity::class.java)
			startActivity(intent)
			finish()
		})
	}

	private fun setupSpinner() {
		ArrayAdapter.createFromResource(
			this,
			R.array.locateOptions,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			// Apply the adapter to the spinner
			spinnerLocale.adapter = adapter
		}

		ArrayAdapter.createFromResource(
			this,
			R.array.projectTypeOptions,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			// Apply the adapter to the spinner
			spinnerTypeProyect.adapter = adapter
		}
	}

	private fun setupButton() {
		continueButton.setOnClickListener {
			onboardingSetupViewModel.setup(
				spinnerLocale.selectedItem.toString(),
				spinnerTypeProyect.selectedItem.toString()
			)
		}
	}
}
