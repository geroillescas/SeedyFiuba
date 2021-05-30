package com.fiuba.seedyfiuba.projects.view.fragments

import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.databinding.FragmentAbstractProjectBinding

class EditProjectFragment : AbstractProjectFragment() {

	override fun setupView(view: FragmentAbstractProjectBinding) {
		super.setupView(view)
		activity?.setTitle(R.string.edit)
		view.fragmentAbstractProjectContinueButton.text = getString(R.string.edit)
		view.fragmentAbstractProjectContinueButton.setOnClickListener {
			editProjectViewModel.updateProject()
			activity?.supportFragmentManager?.popBackStack()
		}
	}
}
