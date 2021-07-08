package com.fiuba.seedyfiuba.projects.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.domain.ProjectStatus
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage

class ProjectsRecyclerViewAdapter(
	private var values: List<Project>,
	private val listener: ProjectViewHolderListener
) : RecyclerView.Adapter<ProjectsRecyclerViewAdapter.ProjectViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.fragment_projects_item, parent, false)
		return ProjectViewHolder(view)
	}

	override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
		val item = values[position]
		item.mediaUrls.firstOrNull()?.let { url ->
			if(URLUtil.isValidUrl(url)){
				FirebaseStorage.getInstance()
					.getReferenceFromUrl(url).downloadUrl.addOnSuccessListener {
						Glide.with(holder.itemView.context)
							.load(it)
							.into(holder.mediaImageView)
					}
			}
		}

		holder.titleTextView.text = item.title
		holder.descriptionTextView.text = item.description
		holder.typeTextView.text = item.type.value

		when (AuthenticationManager.session?.user?.profileType) {
			ProfileType.PATROCINADOR -> {
				holder.detailButton.text = "Patrocinar"
				holder.editButton.visibility = View.INVISIBLE
			}

			ProfileType.VEEDOR -> {
				if (item.status == ProjectStatus.PENDING_REVIEWER_CONFIRMATION) {
					holder.pendingReviewerChip.visibility = View.VISIBLE
					holder.detailButton.text = "Aceptar veeduria"
				} else {
					holder.detailButton.text = "Aprobar etapa"
					holder.pendingReviewerChip.visibility = View.GONE
				}
				holder.editButton.visibility = View.INVISIBLE
			}

			else -> {
				if (item.status == ProjectStatus.PENDING_REVIEWER_CONFIRMATION) {
					holder.pendingReviewerChip.visibility = View.VISIBLE
				} else {
					holder.pendingReviewerChip.visibility = View.GONE
				}
			}
		}

		holder.editButton.setOnClickListener { listener.onEdit(position) }
		holder.detailButton.setOnClickListener { listener.onDetail(position) }
	}

	override fun getItemCount(): Int = values.size

	fun setupProjects(projects: List<Project>) {
		this.values = projects
		notifyDataSetChanged()
	}

	inner class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val titleTextView: TextView = view.findViewById(R.id.project_item_title)
		val descriptionTextView: TextView = view.findViewById(R.id.project_item_description)
		val typeTextView: TextView = view.findViewById(R.id.project_item_type)
		val mediaImageView: ImageView = view.findViewById(R.id.project_item_media)
		val editButton: MaterialButton = view.findViewById(R.id.project_item_edit_button)
		val detailButton: MaterialButton = view.findViewById(R.id.project_item_detail_button)
		val pendingReviewerChip: Chip = view.findViewById(R.id.project_item_status)
	}

	interface ProjectViewHolderListener {
		fun onDetail(position: Int)
		fun onEdit(position: Int)
	}
}
