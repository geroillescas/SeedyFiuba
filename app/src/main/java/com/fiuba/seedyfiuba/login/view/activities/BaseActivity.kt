package com.fiuba.seedyfiuba.login.view.activities

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.fiuba.seedyfiuba.R
import com.mercadopago.smartpos.recharge.viewmodel.BaseViewModel

open class BaseActivity : AppCompatActivity() {

	lateinit var spinner: ConstraintLayout
	lateinit var content: FrameLayout
	lateinit var errorContent: FrameLayout
	lateinit var flipper: ViewFlipper

	open var layoutResource = 0
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_base)
	}

	final override fun setContentView(layoutResID: Int) {
		super.setContentView(layoutResID)

		spinner = findViewById(R.id.spinner)
		content = findViewById(R.id.contentView)
		errorContent = findViewById(R.id.errorContent)
		flipper = findViewById(R.id.flipper)
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
