package com.fiuba.seedyfiuba.profile.view.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.profile.viewmodel.ProfileViewModel
import com.fiuba.seedyfiuba.profile.viewmodel.ProfileViewModelFactory
import com.google.android.material.textfield.TextInputEditText


class ProfileActivity : BaseActivity() {
	private lateinit var name: View
	private lateinit var role: View
	private lateinit var email: View
	private lateinit var description: TextInputEditText

	private val profileViewModel by lazy {
		ViewModelProvider(this, ProfileViewModelFactory()).get(ProfileViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setupObservers()
		setupView()

		setActionBarMode(ActionBarMode.Back)

		findViewById<Button>(R.id.button).setOnClickListener{
			findViewById<TextView>(R.id.profile_name).text= findViewById<TextInputEditText>(R.id.profile_description).text.toString()
		}

	}

	private fun setupObservers() {
		profileViewModel.showLoading.observe(this, Observer {
			if(it){
				setViewState(ViewState.Loading)
			}else{
				setViewState(ViewState.Success)
			}
		})

	}

	private fun setupView() {
		name = findViewById(R.id.profile_name)
		role = findViewById(R.id.profile_role)
		email = findViewById(R.id.profile_email)
		description = findViewById(R.id.profile_description)
	}

	override var layoutResource: Int = R.layout.activity_profile

}

