package com.fiuba.seedyfiuba.projects.view.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.projects.domain.Project
import com.google.android.material.button.MaterialButton
import com.google.firebase.storage.FirebaseStorage

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
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
			FirebaseStorage.getInstance()
				.getReferenceFromUrl(url).downloadUrl.addOnSuccessListener {
				Glide.with(holder.itemView.context)
					.load(it)
					.into(holder.mediaImageView)
			}
		}

		holder.titleTextView.text = item.title
		holder.descriptionTextView.text = item.description
		holder.typeTextView.text = item.type.value

		holder.editButton.setOnClickListener { listener.onEdit(position) }
		holder.deleteButton.setOnClickListener { listener.onDeleted(position) }
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
		val deleteButton: MaterialButton = view.findViewById(R.id.project_item_delete_button)
	}

	interface ProjectViewHolderListener {
		fun onDeleted(position: Int)
		fun onEdit(position: Int)
	}
}
