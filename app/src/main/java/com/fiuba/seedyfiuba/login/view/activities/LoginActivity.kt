package com.fiuba.seedyfiuba.login.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.login.domain.Session
import com.fiuba.seedyfiuba.login.viewmodel.LoginViewModel
import com.fiuba.seedyfiuba.login.viewmodel.LoginViewModelFactory
import com.fiuba.seedyfiuba.profile.view.activities.ContactActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : BaseActivity() {

	private lateinit var mGoogleSignInClient: GoogleSignInClient
	private lateinit var auth: FirebaseAuth


	private lateinit var email: TextInputEditText
	private lateinit var password: TextInputEditText
	private lateinit var emailContainer: TextInputLayout
	private lateinit var passwordContainer: TextInputLayout

	private val loginViewModel by lazy {
		ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
	}

	override var layoutResource: Int = R.layout.activity_login

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = FirebaseAuth.getInstance()
		setActionBarMode(ActionBarMode.None)

		setupView()
		val login = findViewById<Button>(R.id.login)
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

		findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
			setViewState(ViewState.Loading)
			val signInIntent = mGoogleSignInClient.signInIntent
			startActivityForResult(signInIntent, RC_SIGN_IN)
		}

		findViewById<Button>(R.id.register).setOnClickListener {
			val registerIntent = Intent(this, RegisterActivity::class.java)
			startActivityForResult(registerIntent, RC_REGISTER)
		}


		loginViewModel.loginFormState.observe(this, Observer {
			// disable login button unless both username / password is valid
			login.isEnabled = it.isDataValid
			emailContainer.error = it.usernameError?.let { it1 -> getString(it1) }
			passwordContainer.error = it.passwordError?.let { it1 -> getString(it1) }
		})

		loginViewModel.loginResult.observe(this, Observer {
			it.error?.let { it1 -> showLoginFailed(it1) }
			it.success?.let { it1 ->
				updateUiWithUser(it1)
				setResult(Activity.RESULT_OK)
				registerWithFirebase()
				//Complete and destroy login activity once successful
			}


		})

		email.afterTextChanged {
			loginViewModel.loginDataChanged(
				email.text.toString(),
				password.text.toString()
			)

		}

		password.apply {
			afterTextChanged {
				loginViewModel.loginDataChanged(
					email.text.toString(),
					password.text.toString()
				)
			}

			setOnEditorActionListener { _, actionId, _ ->
				when (actionId) {
					EditorInfo.IME_ACTION_DONE ->
						loginViewModel.login(
							email.text.toString(),
							password.text.toString()
						)
				}
				false
			}

			login.setOnClickListener {
				setViewState(ViewState.Loading)
				loginViewModel.login(email.text.toString(), password.text.toString())
			}
		}
	}

	private fun registerWithFirebase(){
		val user = AuthenticationManager.session!!.user
		FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.email).addOnCompleteListener(this) { task ->
			if (task.isSuccessful) {
				// Sign in success, update UI with the signed-in user's information
				Log.d(ContactActivity::class.qualifiedName, "createUserWithEmail:success")
				finish()
			} else {
				// If sign in fails, display a message to the user.
				Log.w(
					ContactActivity::class.qualifiedName,
					"createUserWithEmail:failure",
					task.exception
				)
				finish()
			}
		}
	}

	private fun setupView() {
		email = findViewById(R.id.email_login)
		password = findViewById(R.id.password_login)
		emailContainer = findViewById(R.id.email_login_container)
		passwordContainer = findViewById(R.id.password_login_container)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach
			// a listener.
			val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
			handleSignInResult(task)
		}

		if (requestCode == RC_REGISTER) {
			// The Task returned from this call is always completed, no need to attach
			// a listener.
			val result = data?.getBooleanExtra(RC_REGISTER_EXTRA, false) ?: false
			if (result) {
				Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun updateUiWithUser(model: Session) {
		val welcome = "${getString(R.string.welcome)} ${model.user.name}"
		Toast.makeText(
			applicationContext,
			welcome,
			Toast.LENGTH_LONG
		).show()
	}

	private fun showLoginFailed(@StringRes errorString: Int) {
		setViewState(ViewState.Success)
		Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
	}

	private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
		try {
			val account = completedTask.getResult(ApiException::class.java)
			// Signed in successfully, show authenticated UI.
			loginViewModel.loginGoogle(account?.idToken!!)
		} catch (e: ApiException) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode)
			setViewState(ViewState.Error)
		}
	}

	companion object {
		const val RC_SIGN_IN = 1001
		const val RC_REGISTER = 1002
		const val RC_REGISTER_EXTRA = "RC_REGISTER_EXTRA"
	}
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
	this.addTextChangedListener(object : TextWatcher {
		override fun afterTextChanged(editable: Editable?) {
			afterTextChanged.invoke(editable.toString())
		}

		override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

		override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
	})
}
