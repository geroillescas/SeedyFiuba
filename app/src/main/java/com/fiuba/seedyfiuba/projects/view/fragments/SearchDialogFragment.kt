package com.fiuba.seedyfiuba.projects.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.convertToBigDecimal
import com.fiuba.seedyfiuba.databinding.FragmentSearchDialogBinding
import com.fiuba.seedyfiuba.login.domain.ProjectType
import com.fiuba.seedyfiuba.login.view.activities.afterTextChanged
import com.fiuba.seedyfiuba.projects.domain.LocationProject
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.fiuba.seedyfiuba.projects.domain.SearchForm
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.math.BigDecimal

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    SearchDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class SearchDialogFragment(val listener: SearchDialogFragmentListener) :
	BottomSheetDialogFragment() {

	private lateinit var binding: FragmentSearchDialogBinding
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	private lateinit var checkLocationPermission: ActivityResultLauncher<Array<String>>
	private var searchForm = SearchForm()
	private var locationProject = LocationProject()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
		checkLocationPermission = registerForActivityResult(
			ActivityResultContracts.RequestMultiplePermissions()
		) { permissions ->
			if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
				permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
			) {
				search()
			} else {
				// Permission was denied. Display an error message.
			}
		}
	}

	private fun search() {
		if (ActivityCompat.checkSelfPermission(
				requireActivity(),
				Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
				requireActivity(),
				Manifest.permission.ACCESS_COARSE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			checkLocationPermission.launch(
				arrayOf(
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
				)
			)
			return
		}
		fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
			location?.let {
				locationProject.x = location.latitude.toBigDecimal()
				locationProject.y = location.longitude.toBigDecimal()
				searchForm = searchForm.copy(location = locationProject)
			}
			listener.searchWith(searchForm)
			dismiss()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {

		binding = DataBindingUtil.inflate(
			inflater,
			R.layout.fragment_search_dialog,
			container,
			false
		)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setupView()
	}

	private fun setupView() {
		binding.searchDialogFragmentTypeSpinnerView.let { spinner ->
			spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onNothingSelected(parent: AdapterView<*>?) = Unit
				override fun onItemSelected(
					parent: AdapterView<*>?,
					view: View?,
					position: Int,
					id: Long
				) {
					ProjectType.values()
						.find { it.value == spinner.adapter.getItem(position).toString() }?.let {
							searchForm = searchForm.copy(projectType = it)
						}
				}
			}
		}

		binding.searchDialogFragmentStepsSpinnerView.let { spinner ->
			spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onNothingSelected(parent: AdapterView<*>?) = Unit
				override fun onItemSelected(
					parent: AdapterView<*>?,
					view: View?,
					position: Int,
					id: Long
				) {
					ProjectStatus.values()
						.find { it.value == spinner.adapter.getItem(position).toString() }?.let {
							if (it != ProjectStatus.NONE) {
								searchForm = searchForm.copy(projectStatus = it)
							}
						}
				}
			}
		}

		binding.searchDialogFragmentLatitude.afterTextChanged {
			locationProject.x = it.convertToBigDecimal()
		}

		binding.searchDialogFragmentLongitude.afterTextChanged {
			locationProject.y = it.convertToBigDecimal()
		}

		binding.searchDialogFragmentContinueButton.setOnClickListener {
			searchForm =
				searchForm.copy(hashtag = binding.searchDialogFragmentHashtagsInput.text.toString())
			if (binding.searchDialogFragmentLocationCheckbox.isChecked) {
				search()
			} else {
				if(locationProject.x + locationProject.y != BigDecimal.ZERO ){
					searchForm = searchForm.copy(location = locationProject)
				}
				listener.searchWith(searchForm)
				dismiss()
			}

		}
	}

	interface SearchDialogFragmentListener {
		fun searchWith(
			searchForm: SearchForm
		)
	}
}

