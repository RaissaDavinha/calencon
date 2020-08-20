package com.example.calencon.presentation.chat

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.calencon.R
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_activity.*
import kotlinx.android.synthetic.main.calendar_activity.topAppBar
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarActivity : AppCompatActivity() {
    val boundDays = mutableSetOf<CalendarDay>()
    var boundHeaderMonth: CalendarMonth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
                boundDays.add(day)
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
                } else {
                    container.textView.setTextColor(ContextCompat.getColor(baseContext, R.color.blue_gray))
                }
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        calendarView.updateMonthConfiguration(
            inDateStyle = InDateStyle.ALL_MONTHS,
            maxRowCount = 1,
            hasBoundaries = false
        )

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<ViewContainer> {
            override fun create(view: View) = ViewContainer(view)
            override fun bind(container: ViewContainer, month: CalendarMonth) {
                boundHeaderMonth = month
            }
        }

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
}

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}