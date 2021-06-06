package com.fiuba.seedyfiuba.projects.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.databinding.FragmentAbstractProjectBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CreateProjectFragment : AbstractProjectFragment() {
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	private lateinit var checkLocationPermission: ActivityResultLauncher<Array<String>>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
		checkLocationPermission = registerForActivityResult(
			ActivityResultContracts.RequestMultiplePermissions()
		) { permissions ->
			if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
				permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
			) {
				saveProject()
			} else {
				// Permission was denied. Display an error message.
			}
		}
	}

	override fun setupView(view: FragmentAbstractProjectBinding) {
		super.setupView(view)
		activity?.setTitle(R.string.create_project)
		view.fragmentAbstractProjectContinueButton.text = getString(R.string.continue_label)
		view.fragmentAbstractProjectContinueButton.setOnClickListener {
			saveProject()
		}
	}

	private fun goToStage() {
		editProjectViewModel.project.let {
			val bundle = Bundle().apply {
				putParcelable(ARG_PROJECT, it)
			}
			findNavController().navigate(R.id.stageProjectFragment, bundle)
		}
	}

	private fun saveProject() {
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
				goToStage()
			}
		}

	}

	companion object
}
