package com.fiuba.seedyfiuba.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * This factory should create the required viewModel by type for the view
 */
class HomeViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
			return modelClass.getConstructor(
			).newInstance(
			)
		}
		throw IllegalStateException("ViewModel must extend ${modelClass.name}")
	}
}
