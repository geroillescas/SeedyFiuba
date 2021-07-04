package com.fiuba.seedyfiuba.projects.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.databinding.DetailProjectFragmentBinding
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.view.adapters.DetailProjectsImageRecyclerViewAdapter
import com.fiuba.seedyfiuba.projects.view.adapters.DetailProjectsViewAdapter
import com.fiuba.seedyfiuba.projects.viewmodel.DetailProjectViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.DetailProjectViewModelFactory

open class DetailProjectFragment : Fragment() {
	protected lateinit var binding: DetailProjectFragmentBinding

	protected open val detailProjectViewModel by lazy {
		ViewModelProvider(
			this,
			DetailProjectViewModelFactory()
		).get(DetailProjectViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val project = arguments?.let {
			it.getSerializable(ARG_PROJECT) as Project?
		}

		project?.let { detailProjectViewModel.setupWith(it) }
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = DataBindingUtil.inflate(
			inflater,
			R.layout.detail_project_fragment, container, false
		)
		setupView(binding)

		binding.viewmodel = detailProjectViewModel
		binding.lifecycleOwner = this
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		detailProjectViewModel.showLoading.observe(viewLifecycleOwner, Observer {
			val viewState = if (it) ViewState.Loading else ViewState.Initial
			(requireActivity() as ProjectsActivity).setViewState(viewState)
		})
	}


	protected open fun setupView(binding: DetailProjectFragmentBinding) {
		with(binding.fragmentDetailProjectStages){
			layoutManager = LinearLayoutManager(context)
			val detailProjectsViewAdapter =
				DetailProjectsViewAdapter(
					listOf()
				)
			detailProjectViewModel.project.value?.let {
				detailProjectsViewAdapter.setNewStages(it.stages)
			}

			adapter = detailProjectsViewAdapter
		}

		detailProjectViewModel.project.value?.let {
			if(it.mediaUrls.isEmpty()){
				binding.fragmentDetailProjectImages.visibility = View.GONE
			}else{
				binding.fragmentDetailProjectImages.adapter =
					DetailProjectsImageRecyclerViewAdapter(
						mediaUrls = it.mediaUrls
					)
			}
		}

		binding.fragmentDetailProjectEditButton.setOnClickListener {
			detailProjectViewModel.project.value?.let {
				val bundle = Bundle().apply {
					putParcelable(AbstractProjectFragment.ARG_PROJECT, it)
				}
				findNavController().navigate(R.id.editProjectFragment, bundle)
			}
		}

		binding.fragmentDetailProjectReviewerButton.setOnClickListener {
			detailProjectViewModel.project.value?.let {
				val bundle = Bundle().apply {
					putParcelable(AbstractProjectFragment.ARG_PROJECT, it)
				}
				findNavController().navigate(R.id.reviewerChooserFragment, bundle)
			}
		}

		if(AuthenticationManager.session?.user?.profileType == ProfileType.PATROCINADOR) {
			binding.fragmentDetailProjectEditButton.text = "Patrocinar"
			binding.fragmentDetailProjectReviewerButton.visibility = View.GONE
			binding.fragmentDetailProjectEditButton.setOnClickListener {
				detailProjectViewModel.project.value?.let {
					val bundle = Bundle().apply {
						putParcelable(AbstractProjectFragment.ARG_PROJECT, it)
					}
					findNavController().navigate(R.id.sponsorFragment, bundle)
				}
			}
		}
	}

	companion object {
		const val ARG_PROJECT = "ARG_PROJECT"
	}
}

