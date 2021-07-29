package com.fiuba.seedyfiuba.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.fiuba.seedyfiuba.ActionBarMode
import com.fiuba.seedyfiuba.BaseActivity
import com.fiuba.seedyfiuba.R
import com.fiuba.seedyfiuba.chat.model.ChatViewModel
import com.fiuba.seedyfiuba.chat.model.ChatViewModelFactory
import com.fiuba.seedyfiuba.chat.model.SeedyMessage
import com.fiuba.seedyfiuba.commons.AuthenticationManager
import com.fiuba.seedyfiuba.databinding.ActivityChatBinding
import com.fiuba.seedyfiuba.login.domain.User
import com.fiuba.seedyfiuba.profile.domain.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class ChatActivity : BaseActivity() {
	private lateinit var binding: ActivityChatBinding
	private lateinit var manager: LinearLayoutManager
	private lateinit var profileChat: Profile
	private lateinit var user: User

	// Firebase instance variables
	private lateinit var auth: FirebaseAuth
	private lateinit var db: FirebaseDatabase
	private lateinit var adapter: SeedyMessageAdapter

	private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
		onImageSelected(uri)
	}

	private val chatViewModel by lazy {
		ViewModelProvider(this, ChatViewModelFactory()).get(ChatViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setActionBarMode(ActionBarMode.Back)
		setTitle(R.string.title_activity_chat)

		profileChat = intent.extras?.get(PROFILE) as Profile
		user = AuthenticationManager.session?.user!!
		binding = ActivityChatBinding.inflate(layoutInflater)
		chatViewModel.setup(profileChat, user)
		setContentView(binding.root)

		// When running in debug mode, connect to the Firebase Emulator Suite
		// "10.0.2.2" is a special value which allows the Android emulator to
		// connect to "localhost" on the host computer. The port values are
		// defined in the firebase.json file.
		// Initialize Firebase Auth and check if the user is signed in
		if (initFirebaseAuth()) return

		// Initialize Realtime Database
		db = Firebase.database

		setupRecyclerView()
		setupEditText()
		setupSendButton()
		setupAddMessageImage()
	}

	private fun initFirebaseAuth(): Boolean {
		auth = Firebase.auth
		if (auth.currentUser == null) {
			// Not signed in, launch the Sign In activity
			startActivity(Intent(this, SignInChatActivity::class.java))
			finish()
			return true
		}
		return false
	}

	private fun setupAddMessageImage() {
		binding.addMessageImageView.setOnClickListener {
			openDocument.launch(arrayOf("image/*"))
		}
	}

	private fun setupSendButton() {
		binding.sendButton.setOnClickListener {
			val message = binding.messageEditText.text.toString()
			val friendlyMessage = SeedyMessage(
				message,
				getUserName(),
				getPhotoUrl(),
				null
			)
			setupChild(db.reference)
				.push().setValue(friendlyMessage)
			chatViewModel.sendMessageNotification(message)
			binding.messageEditText.setText("")
		}
	}

	private fun setupEditText() {
		binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))
	}

	private fun setupRecyclerView() {
		val messagesRef = setupChild(db.reference)

		// The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
		// See: https://github.com/firebase/FirebaseUI-Android
		val options = FirebaseRecyclerOptions.Builder<SeedyMessage>()
			.setQuery(messagesRef, SeedyMessage::class.java)
			.build()
		adapter = SeedyMessageAdapter(options, getUserName())
		binding.progressBar.visibility = ProgressBar.INVISIBLE
		manager = LinearLayoutManager(this)
		manager.stackFromEnd = true
		binding.messageRecyclerView.layoutManager = manager
		binding.messageRecyclerView.adapter = adapter

		// Scroll down when a new message arrives
		// See MyScrollToBottomObserver for details
		adapter.registerAdapterDataObserver(
			MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
		)
	}

	public override fun onStart() {
		super.onStart()
		// Check if user is signed in.
		if (auth.currentUser == null) {
			// Not signed in, launch the Sign In activity
			startActivity(Intent(this, SignInChatActivity::class.java))
			finish()
			return
		}
	}

	public override fun onPause() {
		adapter.stopListening()
		super.onPause()
	}

	public override fun onResume() {
		super.onResume()
		adapter.startListening()
	}

	private fun onImageSelected(uri: Uri) {
		Log.d(TAG, "Uri: $uri")
		val tempMessage = SeedyMessage(null, getUserName(), getPhotoUrl(), LOADING_IMAGE_URL)
		setupChild(db.reference)
			.push()
			.setValue(
				tempMessage,
				DatabaseReference.CompletionListener { databaseError, databaseReference ->
					if (databaseError != null) {
						Log.w(
							TAG, "Unable to write message to database.",
							databaseError.toException()
						)
						return@CompletionListener
					}

					// Build a StorageReference and then upload the file
					val key = databaseReference.key
					val storageReference = Firebase.storage
						.getReference(user.userId.toString())
						.child(key!!)
						.child(uri.lastPathSegment!!)
					putImageInStorage(storageReference, uri, key)
				})
	}

	private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
		// First upload the image to Cloud Storage
		storageReference.putFile(uri)
			.addOnSuccessListener(
				this
			) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
				// and add it to the message.
				taskSnapshot.metadata!!.reference!!.downloadUrl
					.addOnSuccessListener { uri ->
						val friendlyMessage =
							SeedyMessage(null, getUserName(), getPhotoUrl(), uri.toString())
						chatViewModel.sendImageNotification()
						setupChild(db.reference)
							.child(key!!)
							.setValue(friendlyMessage)
					}
			}
			.addOnFailureListener(this) { e ->
				Log.w(
					TAG,
					"Image upload task was unsuccessful.",
					e
				)
			}
	}

	private fun getPhotoUrl(): String? {
		val user = auth.currentUser
		return user?.photoUrl?.toString()
	}

	private fun getUserName(): String = "${user.name} ${user.lastName}"

	private fun setupChild(databaseReference: DatabaseReference): DatabaseReference {
		return if (user.userId < profileChat.id) {
			databaseReference.child(MESSAGES_CHILD)
				.child("user_one_${profileChat.id}user_two_${user.userId}")
		} else {
			databaseReference.child(MESSAGES_CHILD)
				.child("user_one_${user.userId}user_two_${profileChat.id}")
		}
	}

	companion object {
		private const val TAG = "MainActivity"
		const val MESSAGES_CHILD = "messages"
		const val ANONYMOUS = "anonymous"
		const val PROFILE = "profile"
		private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
	}
}
