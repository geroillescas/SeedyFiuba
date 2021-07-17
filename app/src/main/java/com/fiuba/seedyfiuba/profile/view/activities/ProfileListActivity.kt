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
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.fiuba.seedyfiuba.profile.view.adapters.ProfileRecyclerViewAdapter
import com.fiuba.seedyfiuba.profile.viewmodel.ProfileViewModel
import com.fiuba.seedyfiuba.profile.viewmodel.ProfileViewModelFactory
import com.fiuba.seedyfiuba.projects.view.adapters.RecyclerViewLoadMoreScroll

open class ProfileListActivity : BaseActivity(),
	ProfileRecyclerViewAdapter.ProfileRecyclerViewAdapterListener {

	private lateinit var scrollListener: RecyclerViewLoadMoreScroll
	private lateinit var profileRecyclerViewAdapter: ProfileRecyclerViewAdapter
	protected val profileListViewModel by lazy {
		ViewModelProvider(this, ProfileViewModelFactory()).get(ProfileViewModel::class.java)
	}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		setupView()
		setupObservers()
		setActionBarMode(ActionBarMode.Back)
		fetchMoreInfo()
    }

	private fun setupObservers() {
		profileListViewModel.showLoading.observe(this, {
			if(it){
				setViewState(ViewState.Loading)
			}else{
				setViewState(ViewState.Success)
			}
		})

		profileListViewModel.profileListLiveData.observe(this, {
			profileRecyclerViewAdapter.setNewProfiles(it)
			scrollListener.setLoaded(false)
		})
	}

	private fun setupView() {
		val profileList = findViewById<RecyclerView>(R.id.activityProfileList_list)
		profileRecyclerViewAdapter = ProfileRecyclerViewAdapter(mutableListOf(), this)
		profileList.adapter = profileRecyclerViewAdapter
		profileList.layoutManager = LinearLayoutManager(this).also {
			scrollListener = RecyclerViewLoadMoreScroll(it)
		}

		scrollListener.setOnLoadMoreListener(object :
			RecyclerViewLoadMoreScroll.OnLoadMoreListener {
			override fun onLoadMore() {
				fetchMoreInfo()
			}
		})
		profileList.addOnScrollListener(scrollListener)
	}

	protected open fun fetchMoreInfo() {
		profileListViewModel.getListProfiles()
	}

	override var layoutResource: Int = R.layout.activity_profile_list

	override fun onProfileClicked(profile: Profile, position: Int) = Unit
}
