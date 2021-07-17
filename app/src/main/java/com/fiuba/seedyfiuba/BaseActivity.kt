package com.fiuba.seedyfiuba

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.profile.view.activities.ContactActivity
import com.fiuba.seedyfiuba.profile.view.activities.ProfileActivity
import com.fiuba.seedyfiuba.profile.view.activities.ProfileListActivity
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class BaseActivity : AppCompatActivity() {

	private var actionBarMode: ActionBarMode =
		ActionBarMode.Back
	lateinit var loadingView: ConstraintLayout
	lateinit var content: FrameLayout
	lateinit var errorContent: ConstraintLayout
	lateinit var flipper: ViewFlipper
	lateinit var drawerLayout: DrawerLayout
	lateinit var navigationView: NavigationView

	open var layoutResource = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_base)
	}

	final override fun setContentView(layoutResID: Int) {
		super.setContentView(layoutResID)

		loadingView = findViewById(R.id.spinner)
		content = findViewById(R.id.contentView)
		errorContent = findViewById(R.id.errorContent)
		flipper = findViewById(R.id.flipper)
		drawerLayout = findViewById(R.id.drawer_layout)
		navigationView = findViewById(R.id.navigation_view)
		if(layoutResource != 0){
			layoutInflater.inflate(
				layoutResource,
				content
			)
		}
		setupNavigationView()
	}

	fun observeLoading(viewModel: BaseViewModel) {
		viewModel.showLoading.observe(this, Observer {
			setViewState(if (it) ViewState.Loading else ViewState.Success)
		})
	}

	fun setViewState(viewState: ViewState) {
		when (viewState) {
			is ViewState.Initial -> setInitialViewState(viewState)
			is ViewState.Success -> setSuccessViewStateSet(viewState)
			is ViewState.Loading -> setLoadingViewStateSet(viewState)
			is ViewState.Error -> setErrorViewStateSet(viewState)
		}
	}

	fun setActionBarMode(actionBarMode: ActionBarMode) {
		this.actionBarMode = actionBarMode
		when (actionBarMode) {
			is ActionBarMode.Home -> {
				supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
				supportActionBar?.setDisplayHomeAsUpEnabled(true)
			}
			is ActionBarMode.Back -> {
				supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)
				supportActionBar?.setDisplayHomeAsUpEnabled(true)
			}
			is ActionBarMode.None -> {
				supportActionBar?.setDisplayHomeAsUpEnabled(false)
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				return when (actionBarMode) {
					is ActionBarMode.Home -> {
						openDrawer()
						true
					}
					is ActionBarMode.Back -> {
						handleBack()
						true
					}
					is ActionBarMode.None -> {
						true
					}
				}
			}
		}
		return super.onOptionsItemSelected(item)
	}

	fun openDrawer() {
		setActionBarMode(ActionBarMode.Back)
		drawerLayout.openDrawer(Gravity.LEFT)
	}

	fun closeDrawer() {
		setActionBarMode(ActionBarMode.Home)
		drawerLayout.closeDrawer(Gravity.LEFT)
	}

	fun handleBack() {
		if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
			closeDrawer()
		} else {
			onBackPressed()
		}
	}

	open fun setInitialViewState(viewState: ViewState) {
		flipper.displayedChild = viewState.getOrder()
	}

	open fun setSuccessViewStateSet(viewState: ViewState) {
		flipper.displayedChild = viewState.getOrder()
	}

	open fun setLoadingViewStateSet(viewState: ViewState) {
		flipper.displayedChild = viewState.getOrder()
	}

	open fun setErrorViewStateSet(viewState: ViewState) {
		flipper.displayedChild = viewState.getOrder()
	}


	override fun onPrepareOptionsMenu(menu: Menu): Boolean {
		super.onPrepareOptionsMenu(menu)
		if(AuthenticationManager.session?.user?.profileType == ProfileType.VEEDOR){
			menu.removeItem(R.id.chat)
		}
		return true
	}

	private fun setupNavigationView() {
		navigationView.setNavigationItemSelectedListener { item ->
			when (item.itemId) {
				R.id.account -> {
					val intent = Intent(this, ProfileActivity::class.java)
					startActivity(intent)
					closeDrawer()
				}

				R.id.accounts -> {
					val intent = Intent(this, ProfileListActivity::class.java)
					startActivity(intent)
					closeDrawer()
				}

				R.id.projects -> {
					val intent = Intent(this, ProjectsActivity::class.java)
					startActivity(intent)
					closeDrawer()
				}

				R.id.chat -> {
					val intent = Intent(this, ContactActivity::class.java)
					startActivity(intent)
					closeDrawer()
				}


				R.id.logout -> {
					logout()
					return@setNavigationItemSelectedListener true
				}
			}
			true
		}
	}


	private fun logout() {
		AlertDialog.Builder(this).apply {
			setCancelable(true)
			setTitle(getString(R.string.close_session))
			setMessage(getString(R.string.close_session_confirm))
			setPositiveButton(getString(R.string.confirm)) { _, _ ->
				CoroutineScope(Dispatchers.Main.immediate).launch {
					LoginContainer.logoutUseCase.invoke()
					val intent = Intent(this@BaseActivity, MainActivity::class.java)
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent)
				}

			}
			setNegativeButton(getString(R.string.cancel)) { _, _ -> }
		}.create().show()
	}
}


sealed class ViewState {
	object Initial : ViewState() {
		override fun getOrder() = 0
	}

	object Loading : ViewState() {
		override fun getOrder() = 1
	}

	object Success : ViewState() {
		override fun getOrder() = 0
	}

	object Error : ViewState() {
		override fun getOrder() = 2
	}

	open fun getOrder() = 0
}

sealed class ActionBarMode {
	object Home : ActionBarMode()
	object Back : ActionBarMode()
	object None : ActionBarMode()
}
