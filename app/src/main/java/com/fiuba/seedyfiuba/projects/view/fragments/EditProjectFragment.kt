package com.fiuba.seedyfiuba.projects.view.fragments

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.databinding.FragmentAbstractProjectBinding

class EditProjectFragment : AbstractProjectFragment() {

	override fun setupView(view: FragmentAbstractProjectBinding) {
		super.setupView(view)
		activity?.setTitle(R.string.edit)
		view.fragmentAbstractProjectContinueButton.text = getString(R.string.edit)
		view.fragmentAbstractProjectContinueButton.setOnClickListener {
			editProjectViewModel.updateProject()
		}
		view.abstractProjectFragmentDateContainer.visibility = View.GONE

		editProjectViewModel.projectResult.observe(viewLifecycleOwner, {
			findNavController().popBackStack(R.id.projectsListFragment, true)
		})

		editProjectViewModel.validate()
	}
}
