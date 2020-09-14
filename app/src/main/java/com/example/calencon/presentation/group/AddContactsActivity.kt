package com.example.calencon.presentation.group

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calencon.R
import com.example.calencon.data.*
import com.example.calencon.presentation.chat.ChatActivity
import com.example.calencon.presentation.group.adapter.AddContactsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_contacts.*

class AddContactsActivity : AppCompatActivity() {
    private lateinit var contactsAdapter: AddContactsAdapter
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var selectedContacts = mutableListOf<User>()
    private lateinit var group: Group
    private val firebase = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_contacts)
        setAdapter()
        fetchUsers()
        setSupportActionBar(topAppBar)

        intent.extras?.getParcelable<Group>(GROUP_KEY)?.let {
            group = it
        }

        continue_button.setOnClickListener { addContactsAndFinish() }
        topAppBar.setNavigationOnClickListener { finish() }
    }

    private fun fetchUsers() {
        firebase.collection(USERS_DOC)
            .addSnapshotListener { snapshot, exception ->
                exception?.let { return@addSnapshotListener }
                snapshot?.let {
                    for (doc in snapshot) {
                        val currentUser = auth.currentUser
                        val user = doc.toObject(User::class.java)

                        if (currentUser?.uid != user.uid) contactsAdapter.add(user)
                    }
                }
            }
    }

    private fun setAdapter() {
        contactsAdapter = AddContactsAdapter()
        contactsAdapter.setOnItemClickListener{ user ->
            selectedContacts.add(user)
            //TODO add remove option
        }
        contacts_recycler_view.adapter = contactsAdapter
        contacts_recycler_view.layoutManager = LinearLayoutManager(this)
    }

    private fun addContactsAndFinish() {
        if (selectedContacts.size >= 1) {
            group.usersList = selectedContacts
            firebase.collection(GROUP_DOC)
                .document(group.id)
                .set(group)
                .addOnSuccessListener {
                    getEvents()

                    val intent = Intent(baseContext, ChatActivity::class.java)
                    intent.putExtra(GROUP_KEY, group)
                    intent.putExtra(CHAT_TYPE, ChatType.GROUP)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, R.string.update_error, Toast.LENGTH_SHORT).show()
                }
            finish()
        } else  {
            Toast.makeText(baseContext, R.string.min_group_users, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getEvents() {
        selectedContacts.forEach { user ->
            firebase.collection(USERS_DOC)
                .document(user.uid)
                .collection(CALENDAR_DOC)
                .addSnapshotListener { value, error ->
                    error?.let {
                        println("Erro ao puxar calendario de usuarios")
                        return@addSnapshotListener }
                    value?.let {
                        for (doc in value) {
                            val event = doc.toObject(Event::class.java)

                            firebase.collection(GROUP_DOC)
                                .document(group.id)
                                .collection(CALENDAR_DOC)
                                .add(event)
                        }
                    }
                }
        }
    }
}