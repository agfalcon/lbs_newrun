package kr.ac.kumoh.newrun.presentation.record

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.DaySize
import com.kizitonwose.calendar.view.MarginValues
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.domain.model.LatLng
import kr.ac.kumoh.newrun.domain.model.RecordListItem
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class RecordFragment : Fragment() {

    val recordItems = listOf(
        RecordListItem(emptyList<LatLng>(), "오늘", "2023.05.11", "3.8km", "8.8km", "32:14"),
        RecordListItem(emptyList<LatLng>(), "오늘", "2023.05.11", "3.8km", "8.8km", "32:14"),
        RecordListItem(emptyList<LatLng>(), "오늘", "2023.05.11", "3.8km", "8.8km", "32:14"),
        RecordListItem(emptyList<LatLng>(), "오늘", "2023.05.11", "3.8km", "8.8km", "32:14"),
        RecordListItem(emptyList<LatLng>(), "오늘", "2023.05.11", "3.8km", "8.8km", "32:14"),
        )

    private lateinit var calendarView: WeekCalendarView
    private lateinit var monthTextView: TextView
    private lateinit var recordList: RecyclerView
    val emoticonResource = listOf(R.drawable.smile_emo, R.drawable.amaizing_emo, R.drawable.angry_emo);


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthTextView = view.findViewById(R.id.monthTextView)
        recordList = view.findViewById(R.id.recordList)
        val recordAdapter = RecordListAdapter(recordItems)
        recordAdapter.notifyDataSetChanged()
        recordList.adapter = recordAdapter
        recordList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        setCalender(view)
    }

    private fun setCalender(view: View){
        calendarView = view.findViewById(R.id.calendarView)
        calendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {

            override fun bind(container: DayViewContainer, data: WeekDay) {
                container.dayTextView.text = data.date.dayOfMonth.toString()
                container.dayImageVIEW.setImageResource(emoticonResource[0])
            }

            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

        }
        calendarView.weekHeaderBinder = object : WeekHeaderFooterBinder<MonthViewContainer> {
            override fun bind(container: MonthViewContainer, data: Week) {
                // Remember that the header is reused so this will be called for each month.
                // However, the first day of the week will not change so no need to bind
                // the same view every time it is reused.
                monthTextView.text = if(data.days[0].date.toString()[5]=='0'){
                    data.days[0].date.format(
                        DateTimeFormatter.ofPattern("yyyy년 M월"))
                } else {
                    data.days[0].date.format(
                        DateTimeFormatter.ofPattern("yyyy년 MM월"))
                }
                if (container.titlesContainer.tag == null) {
                    container.titlesContainer.tag = data.days
                    container.titlesContainer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)[index]
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                        }
                }
            }

            override fun create(view: View) = MonthViewContainer(view)
        }
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(100).atStartOfMonth() // Adjust as needed
        val endDate = currentMonth.plusMonths(100).atEndOfMonth()  // Adjust as needed
        calendarView.setup(startDate, endDate, daysOfWeek.first())
        calendarView.scrollToWeek(currentDate)
    }

}



class DayViewContainer(view: View) : ViewContainer(view) {
    val dayTextView = view.findViewById<TextView>(R.id.calendarDayText)
    val dayImageVIEW = view.findViewById<ImageView>(R.id.scoreImageView)
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    val titlesContainer = view.findViewById<LinearLayout>(R.id.weekLayout)
}