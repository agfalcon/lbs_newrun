package kr.ac.kumoh.newrun.presentation.record

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.model.MyRecord
import kr.ac.kumoh.newrun.data.model.RecordRequest
import kr.ac.kumoh.newrun.data.repository.MyRecordService
import kr.ac.kumoh.newrun.data.repository.RecordRunningService
import kr.ac.kumoh.newrun.data.repository.StableDiffusionService
import kr.ac.kumoh.newrun.domain.data.UserInfo
import kr.ac.kumoh.newrun.domain.model.LatLng
import kr.ac.kumoh.newrun.domain.model.RecordListItem
import java.lang.Math.round
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

    private lateinit var myRecord : MyRecord
    var selectedMode = 1

    private lateinit var calendarView: WeekCalendarView
    private lateinit var monthTextView: TextView
    private lateinit var recordList: RecyclerView
    private lateinit var recordTextView: TextView
    private lateinit var totalButton: Button
    private lateinit var weekButton: Button
    private lateinit var monthButton: Button
    val emoticonResource = listOf(R.drawable.smile_emo, R.drawable.amaizing_emo, R.drawable.angry_emo)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthTextView = view.findViewById(R.id.monthTextView)
        recordList = view.findViewById(R.id.recordList)
        recordTextView = view.findViewById(R.id.recordTextView)
        totalButton = view.findViewById(R.id.totalButton)
        weekButton = view.findViewById(R.id.weekButton)
        monthButton = view.findViewById(R.id.monthButton)
        val recordAdapter = RecordListAdapter(recordItems)
        recordAdapter.notifyDataSetChanged()
        recordList.adapter = recordAdapter
        recordList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        setCalender(view)
        getMyRecord()
        totalButton.setOnClickListener {
            selectedMode = 1
            totalButton.setTextColor(resources.getColor(R.color.white))
            totalButton.setBackgroundColor(resources.getColor(R.color.main_color))
            weekButton.setTextColor(resources.getColor(R.color.black))
            weekButton.setBackgroundColor(resources.getColor(R.color.white))
            monthButton.setTextColor(resources.getColor(R.color.black))
            monthButton.setBackgroundColor(resources.getColor(R.color.white))
            if(myRecord == null) recordTextView.text = "기록없음"
            else recordTextView.text = (round(myRecord.totalDistance*100)/100.0).toString()
        }

        weekButton.setOnClickListener {
            selectedMode = 2
            totalButton.setTextColor(resources.getColor(R.color.black))
            totalButton.setBackgroundColor(resources.getColor(R.color.white))
            weekButton.setTextColor(resources.getColor(R.color.white))
            weekButton.setBackgroundColor(resources.getColor(R.color.main_color))
            monthButton.setTextColor(resources.getColor(R.color.black))
            monthButton.setBackgroundColor(resources.getColor(R.color.white))
            if(myRecord == null) recordTextView.text = "기록없음"
            else recordTextView.text = (round(myRecord.weeklyDistance*100)/100.0).toString()
        }

        monthButton.setOnClickListener {
            selectedMode = 3
            totalButton.setTextColor(resources.getColor(R.color.black))
            totalButton.setBackgroundColor(resources.getColor(R.color.white))
            monthButton.setTextColor(resources.getColor(R.color.white))
            monthButton.setBackgroundColor(resources.getColor(R.color.main_color))
            weekButton.setTextColor(resources.getColor(R.color.black))
            weekButton.setBackgroundColor(resources.getColor(R.color.white))
            if(myRecord == null) recordTextView.text = "기록없음"
            else recordTextView.text = (round(myRecord.monthlyDistance*100)/100.0).toString()
        }

    }

    private fun getMyRecord() {
        CoroutineScope(Dispatchers.IO).launch {
            myRecord = MyRecordService().getMyRecord(UserInfo.id.toString())
            CoroutineScope(Dispatchers.Main).launch {
                when(selectedMode){
                    1 -> recordTextView.text = (round(myRecord.totalDistance*100)/100.0).toString()
                    2 -> recordTextView.text =(round(myRecord.weeklyDistance*100)/100.0).toString()
                    3 -> recordTextView.text = (round(myRecord.monthlyDistance*100)/100.0).toString()
                }
            }
        }
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