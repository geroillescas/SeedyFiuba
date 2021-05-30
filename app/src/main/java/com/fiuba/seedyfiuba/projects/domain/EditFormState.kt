package com.fiuba.seedyfiuba.projects.domain

/**
 * Data validation state of the login form.
 */
data class EditFormState(
	val titleError: Int? = null,
	val descriptionError: Int? = null,
	val typeError: Int? = null,
	val hashtagsError: Int? = null,
	val amountError: Int? = null,
	val dateError: Int? = null,
	val isDataValid: Boolean = false
)
