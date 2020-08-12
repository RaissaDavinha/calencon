package com.example.calencon.presentation.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calencon.R
import com.example.calencon.data.*
import com.example.calencon.presentation.chat.adapter.ChatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.topAppBar
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var mAdapter: ChatAdapter
    private var mUser: User? = null
    private var mMe: User? = null
    private var mGroup: Group? = null
    private lateinit var chatType: ChatType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        intent.extras?.getSerializable(CHAT_TYPE)?.let {
            chatType = it as ChatType
        }

        FirebaseFirestore.getInstance().collection(USERS_DOC)
            .document(FirebaseAuth.getInstance().uid.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                mMe = documentSnapshot.toObject(User::class.java)
                when (chatType) {
                    ChatType.GROUP -> {
                        intent.extras?.getParcelable<Group>(GROUP_KEY)?.let {
                            mGroup = it
                            supportActionBar?.title = mGroup?.name
                        }
                        setAdapter(true)
                        fetchGroupsMessages()
                    }
                    ChatType.SINGLE -> {
                        intent.extras?.getParcelable<User>(USER_KEY)?.let {
                            mUser = it
                            supportActionBar?.title = mUser?.name
                        }
                        setAdapter(false)
                        fetchSingleMessages()
                    }
                }
            }

        topAppBar.setNavigationOnClickListener { finish() }
        btn_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun setAdapter(isGroup: Boolean) {
        FirebaseAuth.getInstance().uid?.let { uid ->
            mAdapter =
                ChatAdapter(uid)
            mAdapter.setIsGroup(isGroup)
            list_chat.adapter = mAdapter
        }
    }

    private fun sendMessage() {
        if (edit_msg.text.isNotEmpty()) {
            val text = edit_msg.text.toString()
            edit_msg.text = null
            val timestamp = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            val currentDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            mMe?.let { mMe ->
                val message = Message(
                    name = mMe.name,
                    text = text,
                    timestamp = timestamp,
                    fromId = mMe.uid,
                    date = currentDateFormat.format(calendar.time),
                    time = currentTimeFormat.format(calendar.time)
                )
                when (chatType) {
                    ChatType.GROUP -> sendGroupChat(message)
                    ChatType.SINGLE -> sendSingleChat(message)
                }
            }
        }
    }

    private fun fetchSingleMessages() {
        mMe?.let { mMe ->
            mUser?.let { mUser ->
                FirebaseFirestore.getInstance().collection(MESSAGE_DOC)
                    .document(mMe.uid)
                    .collection(mUser.uid)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener { querySnapshot, _ ->
                        querySnapshot?.documentChanges?.let {
                            for (doc in it) {
                                when (doc.type) {
                                    DocumentChange.Type.ADDED -> {
                                        val message =
                                            doc.document.toObject(Message::class.java)
                                        mAdapter.addEntity(message)
                                    }
                                    else -> {
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun fetchGroupsMessages() {
        mGroup?.let { mGroup ->
            FirebaseFirestore.getInstance().collection(GROUP_DOC)
                .document(mGroup.id)
                .collection(MESSAGE_DOC)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot, _ ->
                    querySnapshot?.documentChanges?.let {
                        for (doc in it) {
                            when (doc.type) {
                                DocumentChange.Type.ADDED -> {
                                    val message =
                                        doc.document.toObject(Message::class.java)
                                    mAdapter.addEntity(message)
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun sendSingleChat(message: Message) {
        mMe?.let { mMe ->
            mUser?.let { mUser ->
                FirebaseFirestore.getInstance()
                    .collection(MESSAGE_DOC)
                    .document(mMe.uid)
                    .collection(mUser.uid)
                    .add(message)
                    .addOnSuccessListener {}
                    .addOnFailureListener {}
                FirebaseFirestore.getInstance()
                    .collection(MESSAGE_DOC)
                    .document(mUser.uid)
                    .collection(mMe.uid)
                    .add(message)
                    .addOnSuccessListener {}
                    .addOnFailureListener {}
            }
        }
    }

    private fun sendGroupChat(message: Message) {
        mGroup?.let {
            FirebaseFirestore.getInstance()
                .collection(GROUP_DOC)
                .document(it.id)
                .collection(MESSAGE_DOC)
                .add(message)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
    }
}
