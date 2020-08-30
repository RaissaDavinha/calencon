package com.example.calencon.presentation.group

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
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.groups_fragment.*

class GroupsFragment : Fragment() {
    private lateinit var contactsAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.groups_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        fetchGroups()
    }

    private fun fetchGroups() {
        FirebaseFirestore.getInstance().collection(GROUP_DOC)
            .addSnapshotListener { snapshot, exception ->
                exception?.let { return@addSnapshotListener }
                snapshot?.let {
                    for (doc in snapshot) {
                        val group = doc.toObject(Group::class.java)
                        contactsAdapter.add(GroupItem(group))
                    }
                }
            }
    }

    private fun setAdapter() {
        contactsAdapter = GroupAdapter()
        contactsAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(context, ChatActivity::class.java)
            val groupItem: GroupItem = item as GroupItem
            intent.putExtra(GROUP_KEY, groupItem.group)
            intent.putExtra(CHAT_TYPE, ChatType.GROUP)
            startActivity(intent)
        }
        groups_recycler_view.adapter = contactsAdapter
        groups_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        fun newInstance(): GroupsFragment {
            return GroupsFragment()
        }
    }
}