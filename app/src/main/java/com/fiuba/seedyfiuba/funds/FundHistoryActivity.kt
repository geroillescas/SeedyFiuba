package com.fiuba.seedyfiuba.funds

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.databinding.RecyclerViewFundItemBinding
import com.fiuba.seedyfiuba.funds.domains.Contract
import com.fiuba.seedyfiuba.funds.domains.ContractResponse
import com.fiuba.seedyfiuba.funds.viewmodel.FundsHistoryViewModel
import com.fiuba.seedyfiuba.funds.viewmodel.FundsHistoryViewModelFactory
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.view.activities.ProjectsActivity
import com.fiuba.seedyfiuba.projects.view.adapters.RecyclerViewLoadMoreScroll

class FundHistoryActivity : BaseActivity(),
	FundsRecyclerViewAdapter.ContractsRecyclerViewAdapterListener {
	private lateinit var scrollListener: RecyclerViewLoadMoreScroll
	private lateinit var profileRecyclerViewAdapter: FundsRecyclerViewAdapter

	private lateinit var emptyCase : LinearLayout
	private lateinit var fundHistoryList: RecyclerView

	private val fundsHistoryViewModel by lazy {
		ViewModelProvider(
			this,
			FundsHistoryViewModelFactory()
		).get(FundsHistoryViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setActionBarMode(ActionBarMode.Back)
		setupView()
		setupObservers()
		observeLoading(fundsHistoryViewModel)
		fundsHistoryViewModel.getContracts()
	}

	private fun setupObservers() {
		fundsHistoryViewModel.project.observe(this, {
			goToProject(it)
		})

		fundsHistoryViewModel.contractListLiveData.observe(this, {
			if(it.isEmpty()){
				fundHistoryList.visibility = View.GONE
				emptyCase.visibility = View.VISIBLE
			} else{
				fundHistoryList.visibility = View.VISIBLE
				emptyCase.visibility = View.GONE
				profileRecyclerViewAdapter.setContract(it)
			}
		})
	}

	private fun goToProject(project: Project) {
		val intent = Intent(this, ProjectsActivity::class.java).also {
			it.putExtra(ProjectsActivity.PROJECT, project as Parcelable)
		}
		startActivity(intent)
	}

	private fun setupView() {
		fundHistoryList = findViewById(R.id.activityFundHistory_list)
		emptyCase = findViewById(R.id.activityFundHistory_emptyCase)


		val linearLayoutManager = LinearLayoutManager(this)

		profileRecyclerViewAdapter = FundsRecyclerViewAdapter(mutableListOf(), this)
		scrollListener = RecyclerViewLoadMoreScroll(linearLayoutManager)

		scrollListener.setOnLoadMoreListener(object :
			RecyclerViewLoadMoreScroll.OnLoadMoreListener {
			override fun onLoadMore() {
				fundsHistoryViewModel.getContracts()
			}
		})

		fundHistoryList.adapter = profileRecyclerViewAdapter
		fundHistoryList.layoutManager = linearLayoutManager

		fundHistoryList.addOnScrollListener(scrollListener)
	}

	override var layoutResource: Int = R.layout.activity_fund_history

	override fun onContractClicked(contract: Contract, position: Int) {
		fundsHistoryViewModel.getProject(contract.projectId)
	}
}

class FundsRecyclerViewAdapter(
	private var values: List<ContractResponse>,
	private val contractsRecyclerViewAdapterListener: ContractsRecyclerViewAdapterListener
) : RecyclerView.Adapter<FundsRecyclerViewAdapter.ViewCommonHolder>() {

	fun setContract(list: List<ContractResponse>) {
		values = list
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ViewCommonHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val recyclerViewProfileItemBinding =
			RecyclerViewFundItemBinding.inflate(layoutInflater, parent, false)
		return ViewCommonHolder(recyclerViewProfileItemBinding)
	}


	override fun onBindViewHolder(holder: ViewCommonHolder, position: Int) {
		val contracts = values[position]
		holder.binding.contract = contracts
		holder.binding.executePendingBindings()
		holder.itemView.setOnClickListener {
			contractsRecyclerViewAdapterListener.onContractClicked(contracts.contract, position)
		}
	}

	interface ContractsRecyclerViewAdapterListener {
		fun onContractClicked(contract: Contract, position: Int)
	}

	class ViewCommonHolder(val binding: RecyclerViewFundItemBinding) :
		RecyclerView.ViewHolder(binding.root)

	override fun getItemCount(): Int = values.size

}

