package com.example.calencon.presentation.contacts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calencon.R
import com.example.calencon.data.*
import com.example.calencon.presentation.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.contacts_fragment.*

class ContactsFragment : Fragment() {
    private lateinit var contactsAdapter: GroupAdapter<ViewHolder>
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.contacts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        fetchUsers()
    }

    private fun fetchUsers() {
        FirebaseFirestore.getInstance().collection(USERS_DOC)
            .addSnapshotListener { snapshot, exception ->
                exception?.let { return@addSnapshotListener }
                snapshot?.let {
                    for (doc in snapshot) {
                        val currentUser = auth.currentUser
                        val user = doc.toObject(User::class.java)

                        if (currentUser?.uid != user.uid) contactsAdapter.add(
                            UserItem(
                                user
                            )
                        )
                    }
                }
            }
    }

    private fun setAdapter() {
        contactsAdapter = GroupAdapter()
        contactsAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(context, ChatActivity::class.java)
            val userItem: UserItem = item as UserItem
            intent.putExtra(CHAT_TYPE, ChatType.SINGLE)
            intent.putExtra(USER_KEY, userItem.mUser)
            startActivity(intent)
        }
        contacts_recycler_view.adapter = contactsAdapter
        contacts_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        fun newInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }
}