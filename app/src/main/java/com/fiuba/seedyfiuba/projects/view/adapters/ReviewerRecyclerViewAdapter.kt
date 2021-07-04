package com.fiuba.seedyfiuba.projects.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.databinding.FragmentReviewerChooserBinding
import com.fiuba.seedyfiuba.databinding.FragmentReviewerChooserProgressBinding
import com.fiuba.seedyfiuba.profile.domain.Profile

class ReviewerRecyclerViewAdapter(
	private var values: MutableList<Profile?>,
	private val reviewerViewHolderListener: ReviewerViewHolderListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		return when (viewType) {
			TYPE_POST -> {
				val binding = FragmentReviewerChooserBinding.inflate(layoutInflater, parent, false)
				ViewCommonHolder(binding)
			}
			else -> {
				val binding =
					FragmentReviewerChooserProgressBinding.inflate(layoutInflater, parent, false)
				ViewHolderProgress(binding)
			}
		}

	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is ViewCommonHolder -> {
				val currentUser = values[position]
				holder.binding.user = currentUser
				holder.binding.executePendingBindings()
				holder.itemView.setOnClickListener {
					currentUser?.let { it1 -> reviewerViewHolderListener.onReviewerSelected(it1) }
				}
			}
		}
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

	class ViewCommonHolder(val binding: FragmentReviewerChooserBinding) :
		RecyclerView.ViewHolder(binding.root)

	class ViewHolderProgress(val binding: FragmentReviewerChooserProgressBinding) :
		RecyclerView.ViewHolder(binding.root)

	interface ReviewerViewHolderListener {
		fun onReviewerSelected(profile: Profile)
	}

	companion object {
		const val TYPE_LOADER = 2
		const val TYPE_POST = 3
	}
}


