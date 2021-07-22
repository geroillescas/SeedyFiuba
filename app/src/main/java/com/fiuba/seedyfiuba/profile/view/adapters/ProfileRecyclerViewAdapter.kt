package com.fiuba.seedyfiuba.profile.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.databinding.RecyclerViewProfileItemBinding
import com.fiuba.seedyfiuba.databinding.RecyclerViewProfileItemProgressBinding
import com.fiuba.seedyfiuba.profile.domain.Profile
import kotlinx.android.synthetic.main.recycler_view_profile_item.view.*

class ProfileRecyclerViewAdapter(
	private var values: MutableList<Profile?>,
	private val profileRecyclerViewAdapterListener: ProfileRecyclerViewAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	fun setNewProfiles(list: List<Profile>) {
		values = list.toMutableList()
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): RecyclerView.ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		return when (viewType) {
			TYPE_POST -> {
				val binding = RecyclerViewProfileItemBinding.inflate(layoutInflater, parent, false)
				ViewCommonHolder(binding)
			}
			else -> {
				val binding =
					RecyclerViewProfileItemProgressBinding.inflate(layoutInflater, parent, false)
				ViewHolderProgress(binding)
			}
		}
	}


	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is ViewCommonHolder -> {
				val item = values[position]
				item?.let {
					holder.itemView.profileItem_fullmname.text = "${it.name} ${it.lastName}"
					holder.itemView.profileItem_email.text = it.email
					//holder.itemView.profileItem_type.text = it.role.name
					holder.itemView.setOnClickListener {
						profileRecyclerViewAdapterListener.onProfileClicked(item, position)
					}
				}
			}
		}
	}

	interface ProfileRecyclerViewAdapterListener {
		fun onProfileClicked(profile: Profile, position: Int)
	}

	override fun getItemCount(): Int = values.size

	fun setupProfiles(profiles: List<Profile>) {
		values = profiles.toMutableList()
		notifyDataSetChanged()
	}

	fun addProfiles(profiles: List<Profile>) {
		values.addAll(profiles)
		notifyDataSetChanged()
	}

	fun showProgressBar(showProgressBar: Boolean) {
		if (showProgressBar) {
			addLoadingView()
		} else {
			removeLoadingView()
		}
	}

	private fun addLoadingView() {
		values.add(null)
		notifyItemInserted(values.size - 1)
	}

	override fun getItemViewType(position: Int): Int {
		return when {
			values[position] != null -> TYPE_POST
			else -> TYPE_LOADER
		}
	}

	private fun removeLoadingView() {
		//Remove loading item
		values.removeAt(values.size - 1)
		notifyItemRemoved(values.size)
	}

	class ViewCommonHolder(val binding: RecyclerViewProfileItemBinding) :
		RecyclerView.ViewHolder(binding.root)

	class ViewHolderProgress(val binding: RecyclerViewProfileItemProgressBinding) :
		RecyclerView.ViewHolder(binding.root)

	companion object {
		const val TYPE_LOADER = 2
		const val TYPE_POST = 3
	}
}

