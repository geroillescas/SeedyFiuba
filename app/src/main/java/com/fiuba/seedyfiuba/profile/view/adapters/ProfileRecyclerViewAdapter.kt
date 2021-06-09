package com.fiuba.seedyfiuba.profile.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.profile.domain.Profile

class ProfileRecyclerViewAdapter(
	private var values: List<Profile>
) : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfilePreferencesViewHolder>() {

	fun setNewProfiles(list: List<Profile>){
		values = list
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ProfilePreferencesViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.recycler_view_profile_item, parent, false)
		return ProfilePreferencesViewHolder(view)
	}


	override fun onBindViewHolder(holder: ProfilePreferencesViewHolder, position: Int) {
		val item = values[position]
		holder.fullname.text = "${item.name} ${item.lastName}"
		holder.email.text = item.email
		if(item.role != null) {
			holder.type.text = item.role.value
		}

	}

	override fun getItemCount(): Int = values.size

	inner class ProfilePreferencesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val fullname: TextView = view.findViewById(R.id.profileItem_fullmname)
		val email: TextView = view.findViewById(R.id.profileItem_email)
		val type: TextView = view.findViewById(R.id.profileItem_type)
	}
}

