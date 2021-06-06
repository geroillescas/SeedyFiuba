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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.commons.convertToList
import com.fiuba.seedyfiuba.databinding.FragmentAbstractProjectBinding
import com.fiuba.seedyfiuba.login.domain.ProjectType
import com.fiuba.seedyfiuba.login.view.activities.afterTextChanged
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.viewmodel.EditProjectViewModel
import com.fiuba.seedyfiuba.projects.viewmodel.EditProjectViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
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
			val viewState = if (it) ViewState.Loading else ViewState.Initial
			(requireActivity() as ProjectsActivity).setViewState(viewState)
		})

		editProjectViewModel.projectLiveData.observe(viewLifecycleOwner, Observer {
			editProjectViewModel.validate()
		})

		editProjectViewModel.editFormState.observe(viewLifecycleOwner, Observer {
			it?.let { editForm ->
				binding.abstractProjectFragmentDescriptionContainer.error =
					editForm.descriptionError?.let { strId -> getString(strId) }
				binding.abstractProjectFragmentTitleContainer.error =
					editForm.titleError?.let { strId -> getString(strId) }
			}
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

		view.abstractProjectFragmentTitleInput.requestFocus()

		view.abstractProjectFragmentDescriptionInput.afterTextChanged {
			editProjectViewModel.registerDescriptionChanged(it)
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(description = it))
		}

		setupDatePicker(view)

		view.abstractProjectFragmentHashtagsInput.afterTextChanged {
			val listHashtags = it.split(',').filter { list -> list.isNotEmpty() }
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(hashtags = listHashtags))
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

	private fun setupDatePicker(view: FragmentAbstractProjectBinding) {
		val calendar = Calendar.getInstance()
		calendar.add(Calendar.DAY_OF_YEAR, 1)
		val tomorrow = calendar.time.time

		val dateRangePicker =
			MaterialDatePicker.Builder.datePicker()
				.setTitleText("Selecciona la fecha de finalizacion")
				.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
				.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
				.setCalendarConstraints(CalendarConstraints.Builder().setOpenAt(tomorrow).build())
				.build()
		view.abstractProjectFragmentEditDate.setOnClickListener {
			dateRangePicker.show(activity?.supportFragmentManager!!, "DATE")
		}

		dateRangePicker.addOnPositiveButtonClickListener {
			val date = Date(it)
			val format = SimpleDateFormat(getString(R.string.datePattern))
			view.abstractProjectFragmentDateContainer.text =
				SpannableStringBuilder.valueOf(format.format(date))
			editProjectViewModel.setupWith(editProjectViewModel.project.copy(finishDate = date))
		}
	}

	companion object {
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



