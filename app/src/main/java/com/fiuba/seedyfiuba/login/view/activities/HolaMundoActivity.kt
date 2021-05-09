package com.fiuba.seedyfiuba.login.view.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.login.viewmodel.HolaMundoViewModel
import com.fiuba.seedyfiuba.login.viewmodel.HolaMundoViewModelFactory
import com.google.android.material.textfield.TextInputEditText

class HolaMundoActivity : BaseActivity() {

	lateinit var nameTextView: TextInputEditText
	lateinit var fullNameTextView: TextInputEditText
	lateinit var helloWorld: TextView
	lateinit var getButton: Button

	private val holaMundoViewModel by lazy {
		ViewModelProvider(
			this,
			HolaMundoViewModelFactory()
		).get(HolaMundoViewModel::class.java)
	}

	override var layoutResource: Int = R.layout.activity_hola_mundo

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setUpView()
		setActionBarMode(ActionBarMode.Home)
		observeLoading(holaMundoViewModel)
		holaMundoViewModel.holaMundo.observe(this, Observer {
			Toast.makeText(this, it.fullName, LENGTH_LONG)
			helloWorld.text = it.fullName
			nameTextView.text?.clear()
			fullNameTextView.text?.clear()

		})
	}


	private fun setUpView() {
		nameTextView = findViewById(R.id.nameTextView)
		fullNameTextView = findViewById(R.id.fullnameTextView)
		getButton = findViewById(R.id.getButton)
		helloWorld = findViewById(R.id.helloWordTextView)

		getButton.setOnClickListener {
			holaMundoViewModel.getHolaMundo(
				nameTextView.text.toString(),
				fullNameTextView.text.toString()
			)
		}
	}
}
