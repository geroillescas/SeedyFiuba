package com.fiuba.seedyfiuba.projects.view.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.projects.domain.Stages
import com.fiuba.seedyfiuba.projects.view.helpers.ItemTouchHelperAdapter
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class StageProjectsViewAdapter(
	private var values: List<Stages>,
	private val listener: StagesActionListener
) : RecyclerView.Adapter<StageProjectsViewAdapter.ViewHolder>(), ItemTouchHelperAdapter {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.fragment_stage_project_item, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = values[position]
		holder.title.text = item.track
		holder.amount.text = "Monto $ ${item.targetAmount}"
		holder.itemView.setOnTouchListener { view, event ->
			if (event.actionMasked == MotionEvent.ACTION_DOWN) {
				listener.onDragStarted(holder)
			}
			false
		}
	}

	override fun getItemCount(): Int = values.size

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val title: TextView = view.findViewById(R.id.stageProjectFragmentItem_title)
		val amount: TextView = view.findViewById(R.id.stageProjectFragmentItem_amount)
	}

	override fun onItemDismiss(position: Int) {
		listener.onStagesDropped(position)
	}

	override fun onItemMove(fromPosition: Int, toPosition: Int) {
		if (fromPosition < toPosition) {
			for (i in fromPosition until toPosition) {
				swap(i, i + 1)
			}
		} else {
			for (i in fromPosition downTo toPosition + 1) {
				swap(i, i - 1)
			}
		}
		notifyItemMoved(fromPosition, toPosition)
		listener.onStagesReordered(values)
	}


	private fun swap(fromPosition: Int, toPosition: Int) {
		Collections.swap(values, fromPosition, toPosition)
	}

	fun setNewStages(list: List<Stages>) {
		values = list
		notifyDataSetChanged()
	}

	interface StagesActionListener {
		fun onStagesReordered(variants: List<Stages>)
		fun onStagesDropped(position: Int)
		fun onDragStarted(view: ViewHolder)
	}
}
