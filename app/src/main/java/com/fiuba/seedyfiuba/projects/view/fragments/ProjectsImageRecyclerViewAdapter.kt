package com.fiuba.seedyfiuba.projects.view.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiuba.seedyfiuba.R
import com.google.firebase.storage.FirebaseStorage

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ProjectsImageRecyclerViewAdapter(
	private var mediaUrls: List<String>,
	private val listener: ProjectsImageViewHolderListener
) : RecyclerView.Adapter<ProjectsImageRecyclerViewAdapter.ProjectsImageViewHolder>() {

	fun setupNewMediaUrls(mediaUrls: List<String>) {
		this.mediaUrls = mediaUrls
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsImageViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.fragment_projects_image_preview, parent, false)
		return ProjectsImageViewHolder(view)
	}

	override fun onBindViewHolder(holder: ProjectsImageViewHolder, position: Int) {
		val url = mediaUrls[position]

		FirebaseStorage.getInstance().getReferenceFromUrl(url).downloadUrl.addOnSuccessListener {
			Glide.with(holder.itemView.context)
				.load(it)
				.into(holder.projectMedia)
		}


		holder.closeButton.setOnClickListener {
			listener.onCloseSelected(position)
		}
	}

	override fun getItemCount(): Int = mediaUrls.size

	inner class ProjectsImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val closeButton: ImageButton =
			view.findViewById(R.id.fragmentEditProject_image_preview_button)
		val projectMedia: ImageView =
			view.findViewById(R.id.fragmentEditProject_image_preview_media)
	}

	interface ProjectsImageViewHolderListener {
		fun onCloseSelected(position: Int)
	}
}
