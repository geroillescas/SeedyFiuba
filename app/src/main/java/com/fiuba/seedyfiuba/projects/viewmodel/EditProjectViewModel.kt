package com.fiuba.seedyfiuba.projects.viewmodel

import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiuba.seedyfiuba.BaseViewModel
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.projects.domain.EditFormState
import com.fiuba.seedyfiuba.projects.domain.LocationProject
import com.fiuba.seedyfiuba.projects.domain.Project
import com.fiuba.seedyfiuba.projects.usecases.SaveProjectUseCase
import com.fiuba.seedyfiuba.projects.usecases.UpdateProjectUseCase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class EditProjectViewModel(
	private val saveProjectUseCase: SaveProjectUseCase,
	private val updateProjectUseCase: UpdateProjectUseCase
) : BaseViewModel() {
	var storage: FirebaseStorage = FirebaseStorage.getInstance("gs://seedyfiuba.appspot.com")
	private val _editForm = MutableLiveData<EditFormState>()
	val editFormState: LiveData<EditFormState> = _editForm

	private val _mediaUrl = MutableLiveData<MutableList<String>>()
	val mediaUrls: LiveData<MutableList<String>> = _mediaUrl

	private val _project = MutableLiveData<Project>()
	val projectLiveData: LiveData<Project> = _project

	private val _projectResult = MutableLiveData<Project>()
	val projectResult: LiveData<Project> = _projectResult

	var project: Project = Project.newInstance()

	init {
		getLocalSession()
		_project.postValue(project)
		_mediaUrl.postValue(project.mediaUrls)
		_editForm.postValue(EditFormState())
	}

	fun getHashtags(): String {
		return project.hashtags.let {
			if (it.isNotEmpty()) {
				it.reduce { acc, s -> "$acc, $s" }
			} else {
				""
			}
		}
	}

	fun setupWith(project: Project) {
		this.project = project
		_project.postValue(project)
	}

	fun registerTitleChanged(title: String) {
		if (!title.isNotBlank()) {
			_editForm.value = EditFormState(titleError = R.string.invalid_title)
		} else {
			_editForm.value = EditFormState()
		}
	}

	fun registerDescriptionChanged(description: String) {
		if (!description.isNotBlank()) {
			_editForm.value = EditFormState(descriptionError = R.string.invalid_description)
		} else {
			_editForm.value = EditFormState()
		}
	}

	fun uploadImages(images: List<Uri>?) {
		mShowLoading.postValue(true)
		images?.let { list ->
			val storageRef = storage.reference
			for (image in list) {
				val imageRef =
					storageRef.child("images/${session.value?.user?.userId}/${image.path}")
				imageRef.putFile(image).addOnCompleteListener {
					imageRef.downloadUrl.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							val downloadPath = task.result?.toString()!!
							Log.i(
								"EditProjectViewModel",
								"Image successfully uploaded $downloadPath"
							)

							_mediaUrl.value?.let {
								it.add(downloadPath)
								_mediaUrl.postValue(it)
								setupWith(project.copy(mediaUrls = it))
							} ?: run {
								_mediaUrl.postValue(mutableListOf(downloadPath))
								setupWith(project.copy(mediaUrls = mutableListOf(downloadPath)))
							}

							if (_mediaUrl.value?.size!! >= images.size) {
								mShowLoading.postValue(false)
							}
						} else {
							Log.e("EditProjectViewModel", "Error uploading image ${task.exception}")
						}
					}
				}
			}
		}
	}

	fun removeMediaUrlAt(position: Int) {
		_mediaUrl.value?.let {
			it.removeAt(position)
			_mediaUrl.postValue(it)
			setupWith(project.copy(mediaUrls = it))
		}
	}


	fun saveProject(location: Location) {
		launch {
			mShowLoading.postValue(true)
			val locationProject = LocationProject(
				location.latitude.toBigDecimal(),
				location.longitude.toBigDecimal()
			)
			val project = project.copy(location = locationProject)
			when (val result = saveProjectUseCase.invoke(project)) {
				is Result.Success -> {
					_projectResult.postValue(result.data)
					mShowLoading.postValue(false)
				}
				is Result.Error -> {
					mShowLoading.postValue(false)
					_error.postValue(true)
				}
			}
		}
	}

	fun updateProject() {
		launch {
			mShowLoading.postValue(true)
			when (val result = updateProjectUseCase.invoke(project)) {
				is Result.Success -> {
					_projectResult.postValue(result.data)
					mShowLoading.postValue(false)
				}
				is Result.Error -> {
					mShowLoading.postValue(false)
					_error.postValue(true)
				}
			}
		}
	}

	fun validate() {
		project.let {
			if (it.description.isNotBlank() &&
				it.title.isNotBlank() &&
				it.description.length >= 10 &&
				it.finishDate.after(Date())
			) {
				_editForm.postValue(editFormState.value?.copy(isDataValid = true))
			}
		}
	}
}
