package com.fiuba.seedyfiuba.commons

import android.annotation.SuppressLint
import android.content.ClipData
import android.net.Uri
import android.text.InputType
import android.view.MotionEvent
import com.fiuba.seedyfiuba.R
import com.google.android.material.textfield.TextInputEditText

@SuppressLint("ClickableViewAccessibility")
fun TextInputEditText.setOnTouchEndDrawableListener(setOnTouchListener: (TextInputEditText) -> Unit) {
	val drawableRight = 2
	setOnTouchListener { _, event ->
		if (event.action == MotionEvent.ACTION_UP) {
			if (event.rawX >= this.right - this.compoundDrawables[drawableRight].bounds.width()) {
				// your action here
				setOnTouchListener(this)
				return@setOnTouchListener true
			}
		}
		false
	}
}

fun TextInputEditText.toggleShowPassword() {
	this.setOnTouchEndDrawableListener {
		when (it.inputType) {
			(InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT) -> {
				setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_light, 0)
				it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
			}

			(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL) -> {
				setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_filled, 0)
				it.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
			}
		}
	}
}

fun ClipData.convertToList(): List<Uri> = (0 until itemCount).map { getItemAt(it).uri }
