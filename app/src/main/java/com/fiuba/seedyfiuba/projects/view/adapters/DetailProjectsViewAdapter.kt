package com.fiuba.seedyfiuba.projects.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.projects.domain.Stages

class DetailProjectsViewAdapter(
	private val currentStageId: Int,
	private var values: List<Stages>
) : RecyclerView.Adapter<DetailProjectsViewAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.fragment_stage_project_item_small, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = values[position]
		holder.title.text = item.track
		holder.amount.text = "Monto $ ${item.targetAmount}"

		when(currentStageId) {
			position -> {
				holder.status.text = "En espera de aprobacion"
				holder.status.visibility = View.VISIBLE
			}
			else -> {
				holder.status.visibility = View.VISIBLE
			}
		}
	}

	override fun getItemCount(): Int = values.size

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val title: TextView = view.findViewById(R.id.stageProjectFragmentItem_title)
		val amount: TextView = view.findViewById(
            R.id.stageProjectFragmentItem_amount
        )
		val status: TextView = view.findViewById(R.id.stageProjectFragmentItem_status)
	}

	fun setNewStages(list: List<Stages>) {
		values = list
		notifyDataSetChanged()
	}
}
