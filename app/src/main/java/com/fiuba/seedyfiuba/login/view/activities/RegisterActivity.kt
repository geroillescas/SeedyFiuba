package com.fiuba.seedyfiuba.login.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.toggleShowPassword
import com.fiuba.seedyfiuba.login.viewmodel.RegisterViewModel
import com.fiuba.seedyfiuba.login.viewmodel.RegisterViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RegisterActivity : BaseActivity() {

	private val registerViewModel by lazy {
		ViewModelProvider(this, RegisterViewModelFactory()).get(RegisterViewModel::class.java)
	}

	private lateinit var username: TextInputEditText
	private lateinit var password: TextInputEditText
	private lateinit var passwordValidation: TextInputEditText

	private lateinit var usernameContainer: TextInputLayout
	private lateinit var passwordContainer: TextInputLayout
	private lateinit var passwordValidationContainer: TextInputLayout

	private lateinit var registerContainer: ConstraintLayout
	private lateinit var registerButton: Button
	private lateinit var spinner: Spinner


	override var layoutResource: Int = R.layout.activity_register

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setActionBarMode(ActionBarMode.Back)
		setupView()
		setupFormObserver()
		setupSpinner()
		setupUsernameView()
		setupPasswordView()
		setupPasswordValidationView()
		setupButton()
		setupResultObserver()
	}

	private fun setupView() {
		username = findViewById(R.id.username_register)
		password = findViewById(R.id.password_register)
		passwordValidation = findViewById(R.id.password_validation_register)

		usernameContainer = findViewById(R.id.username_register_container)
		passwordContainer = findViewById(R.id.password_register_container)
		passwordValidationContainer = findViewById(R.id.password_validation_register_container)

		registerContainer = findViewById(R.id.register_container)
		registerButton = findViewById(R.id.register_btn)
		spinner = findViewById(R.id.dropdown_menu)
	}

	private fun setupFormObserver() {
		registerViewModel.registerFormState.observe(this, Observer {

			// disable login button unless both username / password is valid
			registerButton.isEnabled = it.isDataValid
			usernameContainer.error = it.usernameError?.let { it1 -> getString(it1) }
			passwordContainer.error = it.passwordError?.let { it1 -> getString(it1) }
			passwordValidationContainer.error =
				it.passwordValidationError?.let { it1 -> getString(it1) }
		})

	}

	private fun setupSpinner() {
		ArrayAdapter.createFromResource(
			this,
			R.array.registerOptions,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			// Apply the adapter to the spinner
			spinner.adapter = adapter
		}
	}

	private fun setupUsernameView() {
		username.apply {
			afterTextChanged {
				validate()
			}
		}
	}

	private fun setupPasswordView() {
		password.apply {
			afterTextChanged {
				validate()
			}
		}

		password.toggleShowPassword()
	}

	private fun setupPasswordValidationView() {
		passwordValidation.apply {
			afterTextChanged {
				validate()
			}

			toggleShowPassword()

			setOnEditorActionListener { _, actionId, _ ->
				when (actionId) {
					EditorInfo.IME_ACTION_DONE -> {
						val imm =
							getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
						imm.hideSoftInputFromWindow(registerContainer.windowToken, 0)
					}
				}
				false
			}
		}
	}

	private fun setupButton() {
		registerButton.setOnClickListener {
			registerViewModel.register(
				username = username.text.toString(),
				password = password.text.toString(),
				profileType = spinner.selectedItem.toString()
			)
		}
	}

	private fun validate() {
		registerViewModel.registerDataChanged(
			username.text.toString(),
			password.text.toString(),
			passwordValidation.text.toString(),
			spinner.selectedItem.toString()
		)
	}

	private fun setupResultObserver() {
		registerViewModel.registerResult.observe(this, Observer {
			val intent = Intent().apply {
				putExtra(LoginActivity.RC_REGISTER_EXTRA, true)
			}
			setResult(LoginActivity.RC_REGISTER, intent)
			finish()
		})
	}
}


