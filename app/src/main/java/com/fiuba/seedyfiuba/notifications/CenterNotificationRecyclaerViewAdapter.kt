package com.fiuba.seedyfiuba.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.databinding.RecyclerViewNotificationItemBinding

class CenterNotificationRecyclerViewAdapter(
	private var values: List<SeedyFiubaNotification>,
	private val notificationRecyclerViewAdapterListener: NotificationRecyclerViewAdapterListener
) : RecyclerView.Adapter<CenterNotificationRecyclerViewAdapter.ViewCommonHolder>() {

	fun setNotifications(list: List<SeedyFiubaNotification>) {
		values = list
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ViewCommonHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val recyclerViewProfileItemBinding = RecyclerViewNotificationItemBinding.inflate(layoutInflater, parent, false)
		return ViewCommonHolder(recyclerViewProfileItemBinding)
	}


	override fun onBindViewHolder(holder: ViewCommonHolder, position: Int) {
		val seedyFiubaNotification = values[position]
		holder.binding.notification = seedyFiubaNotification
		holder.binding.executePendingBindings()
		holder.itemView.setOnClickListener {
			notificationRecyclerViewAdapterListener.onNotificationClicked(seedyFiubaNotification, position)
		}
	}

	interface NotificationRecyclerViewAdapterListener {
		fun onNotificationClicked(seedyFiubaNotification: SeedyFiubaNotification, position: Int)
	}

	class ViewCommonHolder(val binding: RecyclerViewNotificationItemBinding) :
		RecyclerView.ViewHolder(binding.root)

	override fun getItemCount(): Int = values.size

}

