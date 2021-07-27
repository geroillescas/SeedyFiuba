package com.fiuba.seedyfiuba.projects.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.databinding.FragmentSponsorBinding
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.view.activities.ReviewerConditionsActivity
import com.fiuba.seedyfiuba.projects.view.activities.SponsorConditionsActivity
import com.fiuba.seedyfiuba.projects.viewmodel.SponsorViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.SponsorViewModelFactory

class SponsorFragment : Fragment() {
	private lateinit var resultLauncherPick: ActivityResultLauncher<Intent>
	lateinit var binding: FragmentSponsorBinding
	private lateinit var project: Project

	private val sponsorViewModel: SponsorViewModel by lazy {
		ViewModelProvider(this, SponsorViewModelFactory()).get(SponsorViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			project = it.getSerializable(ARG_PROJECT) as Project
		}

		sponsorViewModel.project = project

		resultLauncherPick =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				val accepted =
					result.data?.extras?.getBoolean(ReviewerConditionsActivity.EXTRA_RESULT) ?: false
				if (accepted) {
					sponsorViewModel.sponsor(
						binding.fragmentSponsorAmountInput.text.toString().toBigDecimal()
					)
				} else {
					findNavController().popBackStack(R.id.projectsListFragment, true)
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
			binding.fragmentSponsorAmountButton.isEnabled = sponsorViewModel.isValidAmount(it)
		}

		binding.fragmentSponsorAmountButton.setOnClickListener {
			resultLauncherPick.launch(
				Intent(
					requireActivity(),
					SponsorConditionsActivity::class.java
				)
			)
			requireActivity().overridePendingTransition(
				R.anim.abc_slide_in_bottom,
				R.anim.abc_slide_out_top
			)
		}

		setupObservers()
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		sponsorViewModel.getAmountAvailable()
	}

	private fun setupObservers() {
		sponsorViewModel.updated.observe(viewLifecycleOwner, {
			if (it) {
				binding.fragmentSponsorContent.visibility = View.GONE
				binding.fragmentSponsorCongrats.visibility = View.VISIBLE
			}
		})

		sponsorViewModel.showLoading.observe(viewLifecycleOwner, {
			val viewState = if (it) ViewState.Loading else ViewState.Initial
			(requireActivity() as ProjectsActivity).setViewState(viewState)
		})

		sponsorViewModel.amountAvailable.observe(viewLifecycleOwner, {
			binding.fragmentSponsorSubtitule.text = "Tu saldo disponible es de: ${it} ETHS"
		})
	}

	companion object {
		const val ARG_PROJECT = "ARG_PROJECT"
	}
}
