package com.fiuba.seedyfiuba.projects.view.activities

import android.os.Bundle
import android.widget.TextView
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.google.android.material.button.MaterialButton

open class ReviewerConditionsActivity
	: BaseActivity() {
	override var layoutResource: Int = R.layout.activity_reviewer_conditions

	lateinit var titleTextView: TextView
	lateinit var subTitleTextView: TextView
	lateinit var confirmButton: MaterialButton
	lateinit var rejectButton: MaterialButton
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		supportActionBar?.hide()
		titleTextView = findViewById(R.id.reviewerConditionActivity_title)
		subTitleTextView = findViewById(R.id.reviewerConditionActivity_subtitle)
		confirmButton = findViewById(R.id.reviewerConditionActivity_confirm)
		rejectButton = findViewById(R.id.reviewerConditionActivity_reject)

		confirmButton.setOnClickListener {
			intent.putExtra(EXTRA_RESULT, true)
			setResult(RESULT_OK, intent)
			finish()
		}

		rejectButton.setOnClickListener {
			intent.putExtra(EXTRA_RESULT, false)
			setResult(RESULT_OK, intent)
			finish()
		}
	}

	companion object {
		const val EXTRA_RESULT = "REVIEWER_EXTRA_RESULT"
	}
}


