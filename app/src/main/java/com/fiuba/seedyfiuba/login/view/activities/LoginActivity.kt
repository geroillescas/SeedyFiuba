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
import com.fiuba.seedyfiuba.MainActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.login.domain.LoggedInUserView
import com.fiuba.seedyfiuba.login.viewmodel.LoginViewModel
import com.fiuba.seedyfiuba.login.viewmodel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : BaseActivity() {

	private lateinit var mGoogleSignInClient: GoogleSignInClient
	private lateinit var auth: FirebaseAuth
	private val loginViewModel by lazy {
		ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
	}

	override var layoutResource: Int = R.layout.activity_login

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = FirebaseAuth.getInstance()
		setActionBarMode(ActionBarMode.None)

		val username = findViewById<EditText>(R.id.username)
		val password = findViewById<EditText>(R.id.password)
		val login = findViewById<Button>(R.id.login)
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

		findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
			val signInIntent = mGoogleSignInClient.signInIntent
			startActivityForResult(signInIntent, RC_SIGN_IN)
		}

		findViewById<Button>(R.id.register).setOnClickListener {
			val registerIntent = Intent(this, RegisterActivity::class.java)
			startActivityForResult(registerIntent, RC_REGISTER)
		}


		loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
			val loginState = it ?: return@Observer

			// disable login button unless both username / password is valid
			login.isEnabled = loginState.isDataValid

			if (loginState.usernameError != null) {
				username.error = getString(loginState.usernameError)
			}
			if (loginState.passwordError != null) {
				password.error = getString(loginState.passwordError)
			}
		})

		loginViewModel.loginResult.observe(this@LoginActivity, Observer {
			val loginResult = it ?: return@Observer


			if (loginResult.error != null) {
				showLoginFailed(loginResult.error)
			}
			if (loginResult.success != null) {
				updateUiWithUser(loginResult.success)
			}
			setResult(Activity.RESULT_OK)

			//Complete and destroy login activity once successful
			finish()
		})

		username.afterTextChanged {
			loginViewModel.loginDataChanged(
				username.text.toString(),
				password.text.toString()
			)
		}

		password.apply {
			afterTextChanged {
				loginViewModel.loginDataChanged(
					username.text.toString(),
					password.text.toString()
				)
			}

			setOnEditorActionListener { _, actionId, _ ->
				when (actionId) {
					EditorInfo.IME_ACTION_DONE ->
						loginViewModel.login(
							username.text.toString(),
							password.text.toString()
						)
				}
				false
			}

			login.setOnClickListener {
				setViewState(ViewState.Loading)
				loginViewModel.login(username.text.toString(), password.text.toString())
			}
		}
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
				loginSuccessfully(null)
			}
		}
	}

	private fun updateUiWithUser(model: LoggedInUserView) {
		setViewState(ViewState.Success)
		val welcome = getString(R.string.welcome)
		val displayName = model.displayName
		// TODO : initiate successful logged in experience
		Toast.makeText(
			applicationContext,
			"$welcome $displayName",
			Toast.LENGTH_LONG
		).show()
	}

	private fun showLoginFailed(@StringRes errorString: Int) {
		setViewState(ViewState.Error)
		Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
	}

	private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
		try {
			val account = completedTask.getResult(ApiException::class.java)

			// Signed in successfully, show authenticated UI.
			setViewState(ViewState.Success)
			firebaseAuthWithGoogle(account?.idToken!!)
		} catch (e: ApiException) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode)
			setViewState(ViewState.Error)
		}
	}

	private fun firebaseAuthWithGoogle(idToken: String) {
		val credential = GoogleAuthProvider.getCredential(idToken, null)
		auth.signInWithCredential(credential)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					Log.d("LoginActivity", "signInWithCredential:success")
					val user = auth.currentUser
					loginSuccessfully(user)
				} else {
					// If sign in fails, display a message to the user.
					Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
				}
			}
	}

	private fun loginSuccessfully(user: FirebaseUser?) {
		val intent = Intent().apply {
			putExtra(MainActivity.RC_LOGIN_EXTRA, user)
		}
		setResult(MainActivity.RC_LOGIN, intent)
		finish()
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
