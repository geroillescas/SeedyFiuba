package com.fiuba.seedyfiuba.projects.view.activities

import android.os.Bundle

class StageReviewConditionsActivity : ReviewerConditionsActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		titleTextView.text = "¿Deseas aprobar este etapa?"
		subTitleTextView.text =
			"Si lo haces no se podra volver atras. Debes validar que todo lo comprometido en dicha etapa se cumplio acorde a lo pactado por el emprendedor."
		confirmButton.text = "Aceptar etapa"
	}
}
