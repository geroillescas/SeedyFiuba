package com.fiuba.seedyfiuba.projects.view.fragments

import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.databinding.FragmentAbstractProjectBinding

class CreateProjectFragment : AbstractProjectFragment() {
	override fun setupView(view: FragmentAbstractProjectBinding) {
		super.setupView(view)
		activity?.setTitle(R.string.create_project)
		view.fragmentAbstractProjectContinueButton.text = getString(R.string.create_project)
		view.fragmentAbstractProjectContinueButton.setOnClickListener {
			editProjectViewModel.saveProject()
			activity?.supportFragmentManager?.popBackStack()
		}
	}
}
