package com.fiuba.seedyfiuba.profile.view.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.ViewState
import com.fiuba.seedyfiuba.profile.view.adapters.ProfileRecyclerViewAdapter
import com.fiuba.seedyfiuba.profile.viewmodel.ProfileViewModel
import com.fiuba.seedyfiuba.profile.viewmodel.ProfileViewModelFactory

class ProfileListActivity : BaseActivity() {

	private lateinit var profileRecyclerViewAdapter: ProfileRecyclerViewAdapter
	private val profileListViewModel by lazy {
		ViewModelProvider(this, ProfileViewModelFactory()).get(ProfileViewModel::class.java)
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		setupView()
		setupObservers()
		setActionBarMode(ActionBarMode.Back)
		profileListViewModel.getListProfiles()
    }

	private fun setupObservers() {
		profileListViewModel.showLoading.observe(this, Observer {
			if(it){
				setViewState(ViewState.Loading)
			}else{
				setViewState(ViewState.Success)
			}
		})

		profileListViewModel.profileListLiveData.observe(this, Observer {
			profileRecyclerViewAdapter.setNewProfiles(it)
		})
	}

	private fun setupView() {
		val profileList = findViewById<RecyclerView>(R.id.activityProfileList_list)
		profileRecyclerViewAdapter = ProfileRecyclerViewAdapter(listOf())
		profileList.adapter = profileRecyclerViewAdapter
		profileList.layoutManager = LinearLayoutManager(this)
	}

	override var layoutResource: Int = R.layout.activity_profile_list
}
