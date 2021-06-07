package com.fiuba.seedyfiuba.profile.view.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.google.android.material.textfield.TextInputEditText


class ProfileActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setActionBarMode(ActionBarMode.Back)

		findViewById<Button>(R.id.button).setOnClickListener{
			findViewById<TextView>(R.id.textView4).text= findViewById<TextInputEditText>(R.id.name_register).text.toString()
		}

	}

	override var layoutResource: Int = R.layout.activity_profile

}

