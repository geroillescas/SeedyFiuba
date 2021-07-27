package com.fiuba.seedyfiuba.home.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.funds.viewmodel.GetFundsUseCase
import com.fiuba.seedyfiuba.funds.viewmodel.TransferUserCase
import com.fiuba.seedyfiuba.projects.usecases.GetBalanceUseCase
import java.math.BigDecimal

class HomeViewModel(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val transferUseCase: TransferUserCase
) : BaseViewModel() {
    private val _updated = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> = _updated

    private val _amountAvailable = MutableLiveData<BigDecimal>()
    val amountAvailable: LiveData<BigDecimal> = _amountAvailable

    fun transfer(wallet: String, amount: BigDecimal) {
        launch {
            val userId = AuthenticationManager.userId
            when (transferUseCase.invoke(userId, wallet, amount)) {
                is Result.Success -> {
                    _updated.postValue(true)
                }
                is Result.Error -> {
                    _error.postValue(true)
                }
            }

        }
    }

    fun getAmountAvailable(){
        launch {
            val userId = AuthenticationManager.userId
            when (val result = getBalanceUseCase.invoke(userId)) {
                is Result.Success -> {
                    _amountAvailable.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(true)
                }
            }
        }
    }

    fun isValidAmount(amount: Editable?): Boolean {
        amount?.let {
            return it.isNotEmpty() && it.toString().toBigDecimal() <= _amountAvailable.value
        } ?: run  {
            return false
        }
    }
}
