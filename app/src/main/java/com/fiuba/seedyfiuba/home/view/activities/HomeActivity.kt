package com.fiuba.seedyfiuba.home.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModel
import com.fiuba.seedyfiuba.home.viewmodel.HomeViewModelFactory
import com.fiuba.seedyfiuba.login.domain.ProfileType
import com.fiuba.seedyfiuba.notifications.PushHandleActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : BaseActivity() {
    private lateinit var transferButton: MaterialButton
    private lateinit var amountInput: TextInputEditText
    private lateinit var addressInput: TextInputEditText
    private lateinit var subtitle: TextView


    private val homeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)
    }

    override var layoutResource: Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        setupObservers()
        observeLoading(homeViewModel)
        setActionBarMode(ActionBarMode.Home)


        homeViewModel.getLocalSession()
        intent.extras?.get(PushHandleActivity.NOTIFICACION)?.let {
            val newIntent = Intent(this, PushHandleActivity::class.java)
            intent?.extras?.let { newIntent.putExtras(it) }
            startActivity(newIntent)
        }

        if (AuthenticationManager.session?.user?.profileType == ProfileType.PATROCINADOR) {
            homeViewModel.getAmountAvailable()
        } else {
            findViewById<MaterialCardView>(R.id.homeActivity_content).visibility = View.GONE
        }
    }

    private fun setupObservers() {
        homeViewModel.amountAvailable.observe(this, {
            subtitle.text = "Tu saldo disponible es de: ${it} ETHS"
        })

        homeViewModel.updated.observe(this, {
            Toast.makeText(this, "Realizaste tu trasnferencia", Toast.LENGTH_LONG)
        })
    }

    private fun setUpView() {
        transferButton = findViewById(R.id.homeActivity_transferButton)
        amountInput = findViewById(R.id.homeActivity_amountInput)
        addressInput = findViewById(R.id.homeActivity_adressInput)
        subtitle = findViewById(R.id.homeActivity_subtitule)

        amountInput.addTextChangedListener {
            transferButton.isEnabled = homeViewModel.isValidAmount(it) && !addressInput.text.isNullOrEmpty()
        }

        addressInput.addTextChangedListener {
            transferButton.isEnabled = !it.isNullOrEmpty() && homeViewModel.isValidAmount(amountInput.text)
        }

        transferButton.setOnClickListener {
            homeViewModel.transfer(addressInput.text.toString(), amountInput.text.toString().toBigDecimal())
            addressInput.text?.clear()
            amountInput.text?.clear()
        }
    }
}
