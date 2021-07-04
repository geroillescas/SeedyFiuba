package com.fiuba.seedyfiuba.projects.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.databinding.DetailProjectFragmentBinding
import com.fiuba.seedyfiuba.projects.view.activities.ReviewerConditionsActivity
import com.fiuba.seedyfiuba.projects.view.activities.SponsorConditionsActivity
import com.fiuba.seedyfiuba.projects.viewmodel.ReviewerDetailProjectViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.ReviewerDetailProjectViewModelFactory

class ReviewerDetailProjectFragment : DetailProjectFragment() {
	private lateinit var resultLauncherPick: ActivityResultLauncher<Intent>

	override val detailProjectViewModel : ReviewerDetailProjectViewModel by lazy {
		ViewModelProvider(
            this,
			ReviewerDetailProjectViewModelFactory()
        )
            .get(ReviewerDetailProjectViewModel::class.java)
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		resultLauncherPick =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				if (result.resultCode == Activity.RESULT_OK) {
					val acepted = result.data?.getBooleanExtra(ReviewerConditionsActivity.EXTRA_RESULT, false) ?: false
					detailProjectViewModel.setReviewStatus(acepted)
				}
			}
	}
	 override fun setupView(binding: DetailProjectFragmentBinding) {
		 binding.fragmentDetailProjectReviewerButton.visibility =
             View.GONE
		 binding.fragmentDetailProjectEditButton.text = "Aceptar veeduria"
		 binding.fragmentDetailProjectEditButton.setOnClickListener {
			 resultLauncherPick.launch(Intent(requireActivity(), SponsorConditionsActivity::class.java))
			 requireActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
		 }
	}
}
