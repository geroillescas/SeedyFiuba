package com.fiuba.seedyfiuba.login.view.recycler

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.login.domain.ProjectType

import com.fiuba.seedyfiuba.login.view.activities.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [ProjectType].
 */
class ProjectsPreferencesRecyclerViewAdapter(
	private val values: List<ProjectType>
) : RecyclerView.Adapter<ProjectsPreferencesRecyclerViewAdapter.ProjectsPreferencesViewHolder>() {

	private val projectsTypeData: MutableList<ProjectTypeData> =
		values.map { ProjectTypeData(it) }.toMutableList()

	fun getProjectTypeSelected(): List<ProjectType> =
		projectsTypeData.filter { it.selected }.map { it.projectType }.toList()

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ProjectsPreferencesViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.recycler_view_projects_preferences_item, parent, false)
		return ProjectsPreferencesViewHolder(view)
	}

	override fun onBindViewHolder(holder: ProjectsPreferencesViewHolder, position: Int) {
		val item = projectsTypeData[position]
		holder.title.text = item.projectType.value
		holder.checkBox.isSelected = item.selected
		holder.checkBox.setOnClickListener {
			projectsTypeData[position].selected = !projectsTypeData[position].selected
		}
	}

	override fun getItemCount(): Int = values.size

	inner class ProjectsPreferencesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val title: TextView = view.findViewById(R.id.recycler_view_project_preferences_title)
		val checkBox: CheckBox = view.findViewById(R.id.recycler_view_project_preferences_checkbox)
	}

	inner class ProjectTypeData(val projectType: ProjectType, var selected: Boolean = false)
}

