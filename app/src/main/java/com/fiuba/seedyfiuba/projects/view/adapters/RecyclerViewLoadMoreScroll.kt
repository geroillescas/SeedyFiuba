package com.fiuba.seedyfiuba.projects.view.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreScroll(private val layoutManager: LinearLayoutManager) :
	RecyclerView.OnScrollListener() {

	private var visibleThreshold = 4
	private var mOnLoadMoreListener: OnLoadMoreListener? = null
	private var loaded: Boolean = false
		private set
	private var lastVisibleItem: Int = 0
	private var totalItemCount: Int = 0

	fun setLoaded(loaded: Boolean) {
		this.loaded = loaded
	}

	fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
		this.mOnLoadMoreListener = mOnLoadMoreListener
	}

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)

		if (dy <= 0)
			return

		totalItemCount = layoutManager.itemCount

		lastVisibleItem = layoutManager.findLastVisibleItemPosition()

		if (!loaded && totalItemCount <= lastVisibleItem + visibleThreshold) {
			mOnLoadMoreListener?.onLoadMore()
			loaded = true
		}

	}

	interface OnLoadMoreListener {
		fun onLoadMore()
	}
}
