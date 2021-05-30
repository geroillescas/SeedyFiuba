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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.commons.toggleShowPassword
import com.fiuba.seedyfiuba.login.viewmodel.RegisterViewModel
import com.fiuba.seedyfiuba.login.viewmodel.RegisterViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RegisterActivity : BaseActivity() {

	private val registerViewModel by lazy {
		ViewModelProvider(this, RegisterViewModelFactory()).get(RegisterViewModel::class.java)
	}

	private lateinit var name: TextInputEditText
	private lateinit var lastname: TextInputEditText
	private lateinit var email: TextInputEditText
	private lateinit var password: TextInputEditText
	private lateinit var passwordValidation: TextInputEditText

	private lateinit var nameContainer: TextInputLayout
	private lateinit var lastnameContainer: TextInputLayout
	private lateinit var emailContainer: TextInputLayout
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
		setupPasswordValidationView()
		setupButton()
		setupResultObserver()
	}

	private fun setupView() {
		name = findViewById(R.id.name_register)
		lastname = findViewById(R.id.lastname_register)
		email = findViewById(R.id.email_register)
		password = findViewById(R.id.password_register)
		passwordValidation = findViewById(R.id.password_validation_register)

		nameContainer = findViewById(R.id.name_register_container)
		lastnameContainer = findViewById(R.id.lastname_register_container)
		emailContainer = findViewById(R.id.email_register_container)
		passwordContainer = findViewById(R.id.password_register_container)
		passwordValidationContainer = findViewById(R.id.password_validation_register_container)

		registerContainer = findViewById(R.id.register_container)
		registerButton = findViewById(R.id.register_btn)
		spinner = findViewById(R.id.dropdown_menu)
	}

	private fun setupFormObserver() {
		registerViewModel.registerFormState.observe(this, Observer {

			// disable login button unless both name / password is valid
			registerButton.isEnabled = it.isDataValid
			nameContainer.error = it.nameError?.let { it1 -> getString(it1) }
			lastnameContainer.error = it.lastNameError?.let { it1 -> getString(it1) }
			emailContainer.error = it.emailError?.let { it1 -> getString(it1) }
			passwordContainer.error = it.passwordError?.let { it1 -> getString(it1) }
			passwordValidationContainer.error =
				it.passwordValidationError?.let { it1 -> getString(it1) }
		})

		registerViewModel.showError.observe(this, Observer {
			if (it) {
				setViewState(ViewState.Error)
			} else {
				setViewState(ViewState.Initial)
			}
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
		name.apply {
			afterTextChanged {
				registerViewModel.registerNameChanged(it)
			}
		}

		lastname.apply {
			afterTextChanged {
				registerViewModel.registerLastNameChanged(it)
			}
		}

		email.apply {
			afterTextChanged {
				registerViewModel.registerEmailChanged(it)
			}
		}

		password.apply {
			afterTextChanged {
				registerViewModel.registerPassswordChanged(it)
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
				name = name.text.toString(),
				lastname = lastname.text.toString(),
				email = email.text.toString(),
				password = password.text.toString(),
				profileType = spinner.selectedItem.toString()
			)
		}
	}

	private fun validate() {
		registerViewModel.registerDataChanged(
			name = name.text.toString(),
			lastName = lastname.text.toString(),
			email = email.text.toString(),
			password = password.text.toString(),
			passwordValidation = passwordValidation.text.toString(),
			profileType = spinner.selectedItem.toString()
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


