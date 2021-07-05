package com.fiuba.seedyfiuba.projects.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.databinding.FragmentSponsorBinding
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.activities.ReviewerConditionsActivity

class SponsorFragment : Fragment() {
	private lateinit var resultLauncherPick: ActivityResultLauncher<Intent>
	lateinit var binding: FragmentSponsorBinding
	private lateinit var project: Project

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			project = it.getSerializable(ARG_PROJECT) as Project
		}

		resultLauncherPick =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				if (result.resultCode == Activity.RESULT_OK) {
					val acepted =
						result.data?.getBooleanExtra(ReviewerConditionsActivity.EXTRA_RESULT, false)
							?: false
					if (acepted) {
						binding.fragmentSponsorContent.visibility = View.GONE
						binding.fragmentSponsorCongrats.visibility = View.VISIBLE
					} else {
						findNavController().popBackStack(R.id.projectsListFragment, true)
					}
				}
			}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		binding = DataBindingUtil.inflate(
			inflater,
			R.layout.fragment_sponsor, container, false
		)

		binding.fragmentSponsorCongratsButton.setOnClickListener {
			findNavController().popBackStack(R.id.projectsListFragment, true)
		}

		binding.fragmentSponsorAmountInput.addTextChangedListener {
			binding.fragmentSponsorAmountButton.isEnabled = !it.isNullOrEmpty()
		}

		binding.fragmentSponsorAmountButton.setOnClickListener {
			resultLauncherPick.launch(
				Intent(
					requireActivity(),
					ReviewerConditionsActivity::class.java
				)
			)
			requireActivity().overridePendingTransition(
				R.anim.abc_slide_in_bottom,
				R.anim.abc_slide_out_top
			)
		}

		return binding.root
	}

	companion object {
		const val ARG_PROJECT = "ARG_PROJECT"
	}
}
