package com.example.calencon.presentation.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calencon.R
import com.example.calencon.business.geneticAlgorithm.GeneticAlgorithm
import com.example.calencon.data.CALENDAR_DOC
import com.example.calencon.data.CHAT_ID
import com.example.calencon.data.Event
import com.example.calencon.data.GROUP_DOC
import com.example.calencon.presentation.chat.adapter.EventsAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_activity.*
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*


@Suppress("HasPlatformType")
class CalendarActivity : AppCompatActivity() {
    private val firebase = FirebaseFirestore.getInstance()
    val boundDays = mutableSetOf<CalendarDay>()
    var boundHeaderMonth: CalendarMonth? = null
    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val eventsAdapter = EventsAdapter()
    private val today = LocalDate.now()
    private var events = mutableMapOf<LocalDate, List<Event>>()
    private val chatId by lazy {
        intent.getStringExtra(CHAT_ID)
    }

    companion object {
        fun getStartIntent(context: Context, chatId: String) = Intent(
            context,
            CalendarActivity::class.java
        ).apply {
            putExtra(CHAT_ID, chatId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        setCalendarSetup()
        calendarView.post {
            selectDate(today)
        }

        setRecyclerAdapter()
        getFirebaseData()

        topAppBar.setNavigationOnClickListener { finish() }
        topAppBar.setOnMenuItemClickListener { item: MenuItem? ->
            when(item?.itemId) {
                R.id.chat_to_calendar -> {
                    finish()
                    true
                }
                R.id.more -> {
                    true
                }
                else -> false
            }
        }
    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay
        val textView = view.findViewById<TextView>(R.id.calendarDayText)
        val binding = view.findViewById<FrameLayout>(R.id.dayLayout)

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    selectDate(day.date)
                }
            }
        }
    }

    private fun setRecyclerAdapter() {
        events_list.apply {
            layoutManager = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
        }
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { calendarView.notifyDateChanged(it) }
            calendarView.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun findData() {
        val GA = GeneticAlgorithm()
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        eventsAdapter.apply {
            data.clear()
            data.addAll(events[date].orEmpty())
            notifyDataSetChanged()
        }
    }

    private fun getFirebaseData() {
        firebase.collection(GROUP_DOC)
            .document(chatId)
            .collection(CALENDAR_DOC)
            .orderBy("dtstart", Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, error ->
                error?.let {
                    println("Erro ao puxar calendario de usuarios: $it")
                    return@addSnapshotListener }
                querySnapshot?.let {
                    for (doc in it) {
                        val event = doc.toObject(Event::class.java)
                        val date: LocalDate = Instant.ofEpochMilli(event.dtstart).atZone(ZoneId.systemDefault()).toLocalDate()
                        events[date] = events[date].orEmpty().plus(event)
                    }
                }
            }
    }

    private fun setCalendarSetup() {
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                container.textView.text = day.date.dayOfMonth.toString()
                boundDays.add(day)
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(
                        ContextCompat.getColor(
                            baseContext,
                            R.color.black
                        )
                    )
                    container.binding.setBackgroundResource(if (selectedDate == day.date) R.drawable.selected_day else 0)
                } else {
                    container.textView.setTextColor(
                        ContextCompat.getColor(
                            baseContext,
                            R.color.blue_gray
                        )
                    )
                }
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<ViewContainer> {
            override fun create(view: View) = ViewContainer(view)
            override fun bind(container: ViewContainer, month: CalendarMonth) {
                boundHeaderMonth = month
            }
        }

        calendarView.monthScrollListener = { month ->
            val title = monthTitleFormatter.format(month.yearMonth) + " " + month.yearMonth.year
            month_year_text.text = title
        }
    }
}