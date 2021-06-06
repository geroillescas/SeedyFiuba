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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.databinding.FragmentProjectsListBinding
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.viewmodel.ProjectsViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.ProjectsViewModelFactory


/**
 * A fragment representing a list of Items.
 */
class ProjectsListFragment : Fragment(), ProjectsRecyclerViewAdapter.ProjectViewHolderListener,
	SearchDialogFragment.SearchDialogFragmentListener {

	private var isSearched: Boolean = false
	private var columnCount = 1
	private lateinit var binding: FragmentProjectsListBinding

	private val adapter = ProjectsRecyclerViewAdapter(listOf(), this)

	private val projectViewModel by lazy {
		ViewModelProvider(this, ProjectsViewModelFactory()).get(ProjectsViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			columnCount = it.getInt(ARG_COLUMN_COUNT)
		}
	}

	private fun setupObservers() {
		projectViewModel.projects.observe(viewLifecycleOwner, Observer {
			if (it.isNotEmpty()) {
				binding.projectsListFragmentEmptyCase.visibility = View.GONE
			} else {
				binding.projectsListFragmentEmptyCase.visibility = View.VISIBLE
			}

			if (isSearched) {
				binding.projectsListFragmentSearchViewClose.visibility = View.VISIBLE
			}else{
				binding.projectsListFragmentSearchViewClose.visibility = View.GONE
			}

			adapter.setupProjects(it)
		})

		projectViewModel.showLoading.observe(viewLifecycleOwner, Observer {
			val viewState = if (it) ViewState.Loading else ViewState.Initial
			(requireActivity() as ProjectsActivity).setViewState(viewState)
		})
	}

	override fun onResume() {
		super.onResume()
		activity?.setTitle(R.string.title_activity_projects)
		projectViewModel.getProjects()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		binding = DataBindingUtil.inflate(
			inflater,
			R.layout.fragment_projects_list, container, false
		)

		// Set the adapter
		with(binding.projectsListFragmentRecyclerView) {
			layoutManager = when {
				columnCount <= 1 -> LinearLayoutManager(context)
				else -> GridLayoutManager(context, columnCount)
			}
			adapter = this@ProjectsListFragment.adapter
		}

		binding.projectsListFragmentAddButton.setOnClickListener {
			findNavController().navigate(R.id.createProjectFragment)
		}

		binding.projectsListFragmentSearchView.setOnClickListener {
			val searchDialogFragment = SearchDialogFragment(this)
			searchDialogFragment.show(requireActivity().supportFragmentManager, "Sdosuafbs")
		}

		binding.projectsListFragmentSearchViewClose.setOnClickListener {
			isSearched = false
			projectViewModel.getProjects()
		}

		setupObservers()


		return binding.root
	}

	companion object {
		const val ARG_COLUMN_COUNT = "column-count"
	}

	override fun onDeleted(position: Int) {

	}

	override fun onEdit(position: Int) {
		projectViewModel.projects.value?.getOrNull(position)?.let {
			val bundle = Bundle().apply {
				putParcelable(AbstractProjectFragment.ARG_PROJECT, it)
			}
			findNavController().navigate(R.id.editProjectFragment, bundle)
		}
	}

	override fun searchWith(
		searchForm: SearchForm
	) {
		isSearched = true
		projectViewModel.searchProjects(searchForm)
	}
}
