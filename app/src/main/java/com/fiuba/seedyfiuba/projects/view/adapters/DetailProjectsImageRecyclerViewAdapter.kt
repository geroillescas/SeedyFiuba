package com.fiuba.seedyfiuba.projects.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiuba.seedyfiuba.R
import com.google.firebase.storage.FirebaseStorage

class DetailProjectsImageRecyclerViewAdapter(
	private var mediaUrls: List<String>
) : RecyclerView.Adapter<DetailProjectsImageRecyclerViewAdapter.ProjectsImageViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsImageViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.fragment_projects_image_preview, parent, false)
		return ProjectsImageViewHolder(view)
	}

	override fun onBindViewHolder(holder: ProjectsImageViewHolder, position: Int) {
		val url = mediaUrls[position]

		if(URLUtil.isValidUrl(url)){
			FirebaseStorage.getInstance()
				.getReferenceFromUrl(url).downloadUrl.addOnSuccessListener {
					Glide.with(holder.itemView.context)
						.load(it)
						.into(holder.projectMedia)
				}
		}

		holder.closeButton.visibility = View.GONE
	}

	override fun getItemCount(): Int = mediaUrls.size

	inner class ProjectsImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val closeButton: ImageButton =
			view.findViewById(R.id.fragmentEditProject_image_preview_button)
		val projectMedia: ImageView =
			view.findViewById(R.id.fragmentEditProject_image_preview_media)
	}
}
