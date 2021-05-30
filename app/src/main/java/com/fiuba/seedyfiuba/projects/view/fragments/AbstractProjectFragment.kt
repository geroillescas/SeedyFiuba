package com.fiuba.seedyfiuba.projects.view.fragments

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.convertToList
import com.fiuba.seedyfiuba.databinding.FragmentAbstractProjectBinding
import com.fiuba.seedyfiuba.login.domain.ProjectType
import com.fiuba.seedyfiuba.login.view.activities.afterTextChanged
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.viewmodel.EditProjectViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.EditProjectViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AbstractProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class AbstractProjectFragment : Fragment() {
	private var project: Project? = null
	private lateinit var resultLauncherPick: ActivityResultLauncher<Intent>

	lateinit var binding: FragmentAbstractProjectBinding

	protected val editProjectViewModel by lazy {
		ViewModelProvider(this, EditProjectViewModelFactory()).get(EditProjectViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		ActivityCompat.requestPermissions(
			requireActivity(),
			arrayOf(WRITE_EXTERNAL_STORAGE), 1
		)
		project = arguments?.let {
			it.getSerializable(ARG_PROJECT) as Project?
		} ?: Project.newInstance()

		resultLauncherPick =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				if (result.resultCode == Activity.RESULT_OK) {
					val list = result.data?.clipData?.convertToList()
						?: result.data?.data?.let { listOf(it) }
					editProjectViewModel.uploadImages(list)
				}
			}
		project?.let { editProjectViewModel.setupWith(it) }


	}


	@SuppressLint("SimpleDateFormat")
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		binding = DataBindingUtil.inflate(
			inflater,
			R.layout.fragment_abstract_project, container, false
		)
		setupView(binding)
		setupObservers()
		binding.viewmodel = editProjectViewModel
		binding.lifecycleOwner = this
		return binding.root
	}

	private fun setupObservers() {
		editProjectViewModel.showLoading.observe(viewLifecycleOwner, Observer {
			requireActivity().supportFragmentManager.setFragmentResult(
				ProjectsActivity.FR_SHOW_LOADING,
				bundleOf(ProjectsActivity.BUNDLE_KEY to it)
			)
		})

		editProjectViewModel.projectLiveData.observe(viewLifecycleOwner, Observer {
			editProjectViewModel.validate()
		})

	}

	@SuppressLint("SimpleDateFormat")
	protected open fun setupView(view: FragmentAbstractProjectBinding) {
		val adapter = ProjectsImageRecyclerViewAdapter(
			mediaUrls = editProjectViewModel.project.mediaUrls,
			listener = object : ProjectsImageRecyclerViewAdapter.ProjectsImageViewHolderListener {
				override fun onCloseSelected(position: Int) {
					editProjectViewModel.removeMediaUrlAt(position)
				}
			}
		)

		view.fragmentAbstractProjectImage.adapter = adapter
		editProjectViewModel.mediaUrls.observe(viewLifecycleOwner, Observer {
			adapter.setupNewMediaUrls(it.toList())
		})

		view.fragmentAbstractProjectAddImageButton.setOnClickListener {
			val intent = Intent().apply {
				type = "image/* video/*"
				putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
				action = Intent.ACTION_GET_CONTENT
			}
			resultLauncherPick.launch(Intent.createChooser(intent, "Select Picture"))
		}

		view.abstractProjectFragmentTitleInput.afterTextChanged {
			editProjectViewModel.registerTitleChanged(it)
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(title = it))
		}

		view.abstractProjectFragmentDescriptionInput.afterTextChanged {
			editProjectViewModel.registerDescriptionChanged(it)
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(description = it))
		}

		view.abstractProjectFragmentAmountContainerInput.afterTextChanged {
			val amount = if (it.isNotBlank()) {
				BigDecimal(it)
			} else {
				BigDecimal.ZERO
			}
			editProjectViewModel.registerAmountChanged(amount)
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(amount = amount))
		}

		val dateRangePicker =
			MaterialDatePicker.Builder.datePicker()
				.setTitleText("Select dates")
				.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
				.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
				.build()
		view.abstractProjectFragmentDateContainer.setOnClickListener {
			dateRangePicker.show(activity?.supportFragmentManager!!, "DATE")
		}

		dateRangePicker.addOnPositiveButtonClickListener {
			val date = Date(it)
			val format = SimpleDateFormat(getString(R.string.datePattern))
			view.abstractProjectFragmentDateInput.text =
				SpannableStringBuilder.valueOf(format.format(date))
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(date = date))
		}

		view.abstractProjectFragmentHashtagsInput.afterTextChanged {
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(hashtags = it.split(',')))
		}

		ArrayAdapter.createFromResource(
			requireContext(),
			R.array.projectTypeOptions,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			// Apply the adapter to the spinner

			view.abstractProjectFragmentType.adapter = adapter
			view.abstractProjectFragmentType.onItemSelectedListener =
				object : AdapterView.OnItemSelectedListener {
					override fun onNothingSelected(parent: AdapterView<*>?) = Unit
					override fun onItemSelected(
						parent: AdapterView<*>?,
						view: View?,
						position: Int,
						id: Long
					) {
						ProjectType.values()
							.find { it.value == adapter.getItem(position).toString() }
							?.let {
								editProjectViewModel.setupWith(
									editProjectViewModel.project.copy(
										type = it
									)
								)
							}
					}
				}
		}
	}

	companion object {

		private const val IMAGE_RESULT = "IMAGE_RESULT"
		protected const val REQUEST_TAKE_PHOTO = 1
		private const val REQUEST_CAMERA_PERMISSION = 11
		const val ARG_PROJECT = "ARG_PROJECT"

		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment EditProjectFragment.
		 */
		// TODO: Rename and change types and number of parameters
		@JvmStatic
		fun newInstance(project: Project?) =
			AbstractProjectFragment().apply {
				arguments = Bundle().apply {
					putSerializable(ARG_PROJECT, project)
				}
			}
	}
}



