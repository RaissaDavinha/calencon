package com.example.calencon.presentation.contacts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calencon.R
import com.example.calencon.business.geneticAlgorithm.Specimen
import com.example.calencon.data.*
import com.example.calencon.presentation.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.contacts_fragment.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.random.nextInt

class ContactsFragment : Fragment() {
    private lateinit var contactsAdapter: GroupAdapter<ViewHolder>
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contacts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        fetchUsers()
    }

    private fun fetchUsers() {
        val currentUser = auth.currentUser

        FirebaseFirestore.getInstance().collection(USERS_DOC)
            .addSnapshotListener { snapshot, exception ->
                exception?.let { return@addSnapshotListener }
                snapshot?.let {
                    for (doc in snapshot) {
                        val user = doc.toObject(User::class.java)

                        if (currentUser?.uid != user.uid) contactsAdapter.add(
                            UserItemViewHolder(user)
                        )
                    }
                }
            }
    }

    private fun setAdapter() {
        contactsAdapter = GroupAdapter()
        contactsAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(context, ChatActivity::class.java)
            val userItemViewHolder: UserItemViewHolder = item as UserItemViewHolder
            intent.putExtra(CHAT_TYPE, ChatType.SINGLE)
            intent.putExtra(USER_KEY, userItemViewHolder.mUser)
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

    // Generate initial base
    private fun generateInitialPopulation(uid: String) {

        for (i in 0..60) {
            val dtStart = createStartEvent()

            val item = Event(
                user_id = uid,
                calendar_id = 1,
                dtstart = dtStart,
                dtend = createEndEvent(dtStart),
                title = "evento \$i",
                duration = null,
                all_day = false,
                rrule = "",
                rdate = "",
                availability = 0
            )

            FirebaseFirestore.getInstance().collection(USERS_DOC)
                .document(uid)
                .collection(CALENDAR_DOC)
                .add(item)
        }
    }

    // Generate only future dates, with hour between 8-21h, up tp 3 months in the future
    private fun createStartEvent(): Long {
        val aDay = TimeUnit.DAYS.toMillis(1)
        val now = Calendar.getInstance().timeInMillis

        val threeMonthsInFuture = Calendar.getInstance()
        threeMonthsInFuture.timeInMillis = (now + aDay * 100)
        return between(Calendar.getInstance(), threeMonthsInFuture)
    }

    // One hour default
    private fun createEndEvent(startDate: Long): Long {
        val aHour = TimeUnit.HOURS.toMillis(1)
        return startDate + aHour
    }

    private fun between(startInclusive: Calendar, endExclusive: Calendar): Long {
        val newDate = Calendar.getInstance()

        do {
            val randNumber = Random.nextInt(0..1)
            val hour = Random.nextInt(8..21)
            val minute = if (randNumber == 0) 0 else 30
            val day = Random.nextInt(1..28)
            val month = Random.nextInt(
                range = startInclusive.get(Calendar.MONTH)..endExclusive.get(
                    Calendar.MONTH
                )
            )
            newDate.set(startInclusive.get(Calendar.YEAR), month, day, hour, minute)
        } while (newDate.timeInMillis < startInclusive.timeInMillis)

        println("Current_Selection " + newDate.get(Calendar.HOUR_OF_DAY) + ":" + newDate.get(Calendar.MINUTE) + "\t" + newDate.get(Calendar.DAY_OF_MONTH) + "/" + newDate.get(Calendar.MONTH) + "/" + newDate.get(Calendar.YEAR))
        return newDate.timeInMillis
    }
}