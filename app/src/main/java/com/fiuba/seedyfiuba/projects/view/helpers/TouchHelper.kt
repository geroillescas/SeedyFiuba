package com.fiuba.seedyfiuba.projects.view.helpers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class TouchHelper(val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {
	private var isLongPressDragEnabled = false
	private var isSwipeEnabled = false
	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder
	): Int {
		val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
		val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END or ItemTouchHelper.LEFT
		return makeMovementFlags(
			dragFlags,
			swipeFlags
		)
	}

	override fun onMove(
		recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		adapter.onItemMove(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
		return true
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
		adapter.onItemDismiss(viewHolder.absoluteAdapterPosition)
	}

	override fun isLongPressDragEnabled(): Boolean {
		return isLongPressDragEnabled
	}

	fun setLongPressDragEnabled(longPressDragEnabled: Boolean) {
		isLongPressDragEnabled = longPressDragEnabled
	}

	override fun isItemViewSwipeEnabled(): Boolean {
		return isSwipeEnabled
	}

	fun setSwipeEnabled(swipeEnabled: Boolean) {
		isSwipeEnabled = swipeEnabled
	}

}

interface ItemTouchHelperAdapter {
	fun onItemMove(fromPosition: Int, toPosition: Int)
	fun onItemDismiss(position: Int)
}

