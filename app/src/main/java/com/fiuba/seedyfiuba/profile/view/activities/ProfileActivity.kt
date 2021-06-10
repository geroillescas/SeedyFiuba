package com.fiuba.seedyfiuba.profile.view.activities

import android.os.Bundle
import android.text.Editable
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
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : BaseActivity() {
	private lateinit var name: TextView
	private lateinit var role: TextView
	private lateinit var email: TextView
	private lateinit var description: TextView
	private lateinit var descriptionInput: TextInputEditText
	private lateinit var editButton: Button

	private val profileViewModel by lazy {
		ViewModelProvider(this, ProfileViewModelFactory()).get(ProfileViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setupObservers()
		setupView()

		setActionBarMode(ActionBarMode.Back)

		editButton.setOnClickListener{
			profileViewModel.saveProfile(description = descriptionInput.text.toString())
		}

		profileViewModel.getProfile()
	}

	private fun setupObservers() {
		profileViewModel.showLoading.observe(this, Observer {
			if(it){
				setViewState(ViewState.Loading)
			}else{
				setViewState(ViewState.Success)
			}
		})

		profileViewModel.profileLiveData.observe(this, Observer {
			name.text = "${it.name} ${it.lastName}"
			role.text = it.role.name
			email.text = it.email
			description.text = it.description
			descriptionInput.text?.clear()
		})
	}

	private fun setupView() {
		name = findViewById(R.id.profile_name)
		role = findViewById(R.id.profile_role)
		email = findViewById(R.id.profile_email)
		description = findViewById(R.id.profile_description)
		descriptionInput = findViewById(R.id.profile_description_input)
		editButton = findViewById(R.id.profileActivity_button)
	}

	override var layoutResource: Int = R.layout.activity_profile

}

