package com.fiuba.seedyfiuba.projects.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.databinding.DetailProjectFragmentBinding
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.view.activities.ReviewerConditionsActivity
import com.fiuba.seedyfiuba.projects.view.activities.StageReviewConditionsActivity
import com.fiuba.seedyfiuba.projects.viewmodel.ReviewerDetailProjectViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.ReviewerDetailProjectViewModelFactory

class ReviewerDetailProjectFragment : DetailProjectFragment() {
	private lateinit var resultLauncherPick: ActivityResultLauncher<Intent>
	private lateinit var resultLauncherPickStage: ActivityResultLauncher<Intent>

	override val detailProjectViewModel : ReviewerDetailProjectViewModel by lazy {
		ViewModelProvider(
            this,
			ReviewerDetailProjectViewModelFactory()
        )
            .get(ReviewerDetailProjectViewModel::class.java)
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		currentStageId = detailProjectViewModel.project.value?.currentStageId ?: 0
		resultLauncherPick =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				val acepted = result.data?.getBooleanExtra(ReviewerConditionsActivity.EXTRA_RESULT, false) ?: false
				val status = if(acepted) { "approved" } else { "rejected"}
				detailProjectViewModel.setReviewStatus(status)
			}

		resultLauncherPickStage =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				val accepted = result.data?.extras?.getBoolean(ReviewerConditionsActivity.EXTRA_RESULT, false) ?: false
				if(accepted){
					detailProjectViewModel.setStageStatus("approved")
				}else{
					findNavController().popBackStack(R.id.projectsListFragment, true)
				}
			}
	}

	 override fun setupView(binding: DetailProjectFragmentBinding) {
		 super.setupView(binding)
		 val project = detailProjectViewModel.project.value!!
		 binding.fragmentDetailProjectReviewerButton.visibility = View.GONE
		 if (project.status == ProjectStatus.PENDING_REVIEWER_CONFIRMATION) {
			 binding.fragmentDetailProjectEditButton.text = "Aceptar veeduria"
			 binding.fragmentDetailProjectEditButton.setOnClickListener {
				 resultLauncherPick.launch(Intent(requireActivity(), ReviewerConditionsActivity::class.java))
				 requireActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
			 }
		 }else{
			 binding.fragmentDetailProjectEditButton.text = "Aprobar etapa"
			 binding.fragmentDetailProjectEditButton.setOnClickListener {
				 resultLauncherPickStage.launch(Intent(requireActivity(), StageReviewConditionsActivity::class.java))
				 requireActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
			 }
		 }


		 setupObservers()
	}

	private fun setupObservers() {
		detailProjectViewModel.showLoading.observe(viewLifecycleOwner, Observer {
			val viewState = if (it) ViewState.Loading else ViewState.Initial
			(requireActivity() as ProjectsActivity).setViewState(viewState)
		})

		detailProjectViewModel.updated.observe(viewLifecycleOwner, Observer {
			findNavController().popBackStack(R.id.projectsListFragment, true)
		})
	}
}
