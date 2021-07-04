package com.fiuba.seedyfiuba.projects.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.projects.domain.Project
import java.text.SimpleDateFormat

open class DetailProjectViewModel : BaseViewModel() {

	private val _project = MutableLiveData<Project>()
	val project: LiveData<Project> = _project

	fun setupWith(project: Project) {
		_project.value = project
	}

	@SuppressLint("SimpleDateFormat")
	fun getDate(): String {
		val simpleDate = SimpleDateFormat("dd/MM/yyyy")
		return "Fecha de finalizacion ${simpleDate.format(project.value!!.finishDate)}"
	}

	fun getHasthtags(): String {
		return project.value!!.hashtags.joinToString(", ")
	}

}
