package com.fiuba.seedyfiuba.login.view.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.R


open class BaseActivity : AppCompatActivity() {

	lateinit var loadingView: ConstraintLayout
	lateinit var content: FrameLayout
	lateinit var errorContent: ConstraintLayout
	lateinit var flipper: ViewFlipper
	lateinit var drawerLayout: DrawerLayout

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
		layoutInflater.inflate(
			layoutResource,
			content
		)
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
		when (actionBarMode) {
			is ActionBarMode.Home -> {
				supportActionBar?.setDisplayHomeAsUpEnabled(true)
			}
			is ActionBarMode.Back -> {
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
				onBackPressed()
				return true
			}
		}
		return super.onOptionsItemSelected(item)
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
