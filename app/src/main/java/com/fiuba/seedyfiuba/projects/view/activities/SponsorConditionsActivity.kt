package com.fiuba.seedyfiuba.projects.view.activities

import android.os.Bundle

class SponsorConditionsActivity : ReviewerConditionsActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		titleTextView.text = "¿Deseas patrocinar a este proyecto?"
		subTitleTextView.text =
			"Si lo aceptas enotnces debe cumplir con las reglas de impuestas por el proyecto y el egreso de fondos correspondiente." +
					"Tu aporte no se podra revertir una vez que se confirme"
		confirmButton.text = "Aceptar condiciones"
	}
}

