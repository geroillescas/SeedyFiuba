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
import com.fiuba.seedyfiuba.databinding.FragmentReviewerChooserListBinding
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.view.adapters.RecyclerViewLoadMoreScroll
import com.fiuba.seedyfiuba.projects.view.adapters.ReviewerRecyclerViewAdapter
import com.fiuba.seedyfiuba.projects.viewmodel.ReviewerViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.ReviewerViewModelFactory

/**
 * A fragment representing a list of Items.
 */
class ReviewerChooserFragment : Fragment(), ReviewerRecyclerViewAdapter.ReviewerViewHolderListener {
	private lateinit var scrollListener: RecyclerViewLoadMoreScroll
	private var project: Project? = null
	private lateinit var binding: FragmentReviewerChooserListBinding

	private val adapter =
		ReviewerRecyclerViewAdapter(
			mutableListOf(),
			this
		)

	private val reviewerViewModel by lazy {
		ViewModelProvider(this, ReviewerViewModelFactory()).get(ReviewerViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		project = arguments?.let {
			it.getSerializable(ARG_PROJECT) as Project?
		} ?: Project.newInstance()
		reviewerViewModel.project = project!!
	}

	override fun onResume() {
		super.onResume()
		activity?.setTitle(R.string.title_activity_projects)
		if(project?.status == ProjectStatus.IN_PROGRESS) {
			reviewerViewModel.requestStageReview()
		}else {
			reviewerViewModel.getProjects()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = DataBindingUtil.inflate(
			inflater,
			R.layout.fragment_reviewer_chooser_list, container, false
		)

		binding.reviewerChooserList.let {
			it.adapter = adapter
			val linearLayoutManager = LinearLayoutManager(context)
			it.layoutManager = linearLayoutManager

			scrollListener = RecyclerViewLoadMoreScroll(linearLayoutManager)
			scrollListener.setOnLoadMoreListener(object : RecyclerViewLoadMoreScroll.OnLoadMoreListener {
				override fun onLoadMore() {
					reviewerViewModel.getMoreProjects()
				}
			})
			it.addOnScrollListener(scrollListener)
		}

		setupObservers()

		return binding.root
	}

	private fun setupObservers() {
		reviewerViewModel.profile.observe(viewLifecycleOwner, {
			adapter.addProfiles(it)
		})

		reviewerViewModel.showLoadingSpinner.observe(viewLifecycleOwner, {
			adapter.showProgressBar(it)
			scrollListener.setLoaded(it)
		})

		reviewerViewModel.showLoading.observe(viewLifecycleOwner, {
			val viewState = if (it) ViewState.Loading else ViewState.Initial
			(requireActivity() as ProjectsActivity).setViewState(viewState)
		})

		binding.reviewerChooserButton.setOnClickListener {
			findNavController().popBackStack(R.id.projectsListFragment, true)
		}

		reviewerViewModel.updated.observe(viewLifecycleOwner, {
			binding.content.visibility = View.GONE
			binding.congrats.visibility = View.VISIBLE
		})
	}


	override fun onReviewerSelected(profile: Profile) {
		reviewerViewModel.updateProjectWithProfileSelected(profile)
	}

	companion object {
		const val ARG_PROJECT = "ARG_PROJECT"
	}
}
