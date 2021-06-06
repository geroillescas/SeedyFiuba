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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.convertToBigDecimal
import com.fiuba.seedyfiuba.databinding.FragmentStageProjectBinding
import com.fiuba.seedyfiuba.login.view.activities.afterTextChanged
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.Stages
import com.fiuba.seedyfiuba.projects.view.fragments.AbstractProjectFragment.Companion.ARG_PROJECT
import com.fiuba.seedyfiuba.projects.view.helpers.TouchHelper
import com.fiuba.seedyfiuba.projects.viewmodel.StageProjectViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.StageProjectViewModelFactory


/**
 * A fragment representing a list of Items.
 */
class StageProjectFragment : Fragment() {

	private lateinit var itemTouchHelper: ItemTouchHelper
	private lateinit var binding: FragmentStageProjectBinding
	private lateinit var stagesAdapter: StageProjectsViewAdapter

	private val stageProjectViewModel by lazy {
		ViewModelProvider(
			this,
			StageProjectViewModelFactory()
		).get(StageProjectViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			val project = it.getParcelable<Project>(ARG_PROJECT)!!
			stageProjectViewModel.setupWith(project)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = DataBindingUtil.inflate(
			inflater,
			R.layout.fragment_stage_project, container, false
		)

		with(binding) {
			stageProjectFragmentContinueButton.setOnClickListener {
				stageProjectViewModel.saveProject()
			}

			stageProjectFragmentTrackContainerInput.requestFocus()
			stageProjectFragmentTrackContainerInput.afterTextChanged {
				val amount = binding.stageProjectFragmentAmountContainerInput.text.toString()
					.convertToBigDecimal()
				stageProjectViewModel.validateStage(it, amount)
			}

			stageProjectFragmentAmountContainerInput.afterTextChanged {
				val track = binding.stageProjectFragmentTrackContainerInput.text.toString()
				stageProjectViewModel.validateStage(track, it.convertToBigDecimal())
			}

			stageProjectFragmentAddStage.setOnClickListener {
				val track = binding.stageProjectFragmentTrackContainerInput.text.toString()
				val amount =
					binding.stageProjectFragmentAmountContainerInput.text.toString().toBigDecimal()
				stageProjectViewModel.addStage(track, amount)
				stageProjectFragmentAmountContainerInput.text?.clear()
				stageProjectFragmentTrackContainerInput.text?.clear()
			}

			with(list) {
				layoutManager = LinearLayoutManager(context)
				stagesAdapter = StageProjectsViewAdapter(listOf(), object :
					StageProjectsViewAdapter.StagesActionListener {
					override fun onStagesReordered(stages: List<Stages>) {
						stageProjectViewModel.reorderStages(stages)
					}

					override fun onStagesDropped(position: Int) {
						stageProjectViewModel.stagesDrop(position)
					}

					override fun onDragStarted(view: StageProjectsViewAdapter.ViewHolder) {
						itemTouchHelper.startDrag(view)
					}
				})

				val callbacks = TouchHelper(stagesAdapter)
				itemTouchHelper = ItemTouchHelper(callbacks)
				itemTouchHelper.attachToRecyclerView(this)
				adapter = stagesAdapter
			}
		}

		return binding.root
	}

	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
		val list = requireActivity().intent.extras?.getParcelableArrayList<Stages>(STAGE)
		requireActivity().intent.extras?.clear()

		list?.toList()?.let {
			stagesAdapter.setNewStages(it)
			stageProjectViewModel.reorderStages(it)
		}
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		with(stageProjectViewModel) {
			projectResult.observe(viewLifecycleOwner, Observer {
				findNavController().popBackStack(R.id.projectsListFragment, false)
			})

			isAddValid.observe(viewLifecycleOwner, Observer {
				binding.stageProjectFragmentAddStage.isEnabled = it
			})

			isContinueValid.observe(viewLifecycleOwner, Observer {
				binding.stageProjectFragmentContinueButton.isEnabled = it
			})

			stages.observe(viewLifecycleOwner, Observer {
				validateProject()
				stagesAdapter.setNewStages(it.toList())
			})
		}
	}

	override fun onStop() {
		super.onStop()
		val arrayList: ArrayList<Stages> = arrayListOf()
		stageProjectViewModel.stages.value?.let {
			arrayList.addAll(it)
		}

		requireActivity().intent.putParcelableArrayListExtra(STAGE, arrayList)
	}

	companion object {
		private const val STAGE = "STAGES"
	}
}
