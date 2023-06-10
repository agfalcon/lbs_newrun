package kr.ac.kumoh.newrun.presentation.record

import android.os.Bundle
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
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.model.MyRecord
import kr.ac.kumoh.newrun.data.model.RunData
import kr.ac.kumoh.newrun.data.repository.MyRecordService
import kr.ac.kumoh.newrun.domain.data.UserInfo
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale


class RecordFragment : Fragment() {



    private lateinit var myRecord : MyRecord
    private lateinit var runData : List<RunData>
    private lateinit var runningTextView : TextView
    var selectedMode = 1
    var tempDistance: Float = 0f
    var lastDate: String = ""
    var dDay = 0

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
        runningTextView = view.findViewById(R.id.runningTextView)
        getMyRecord()
        getAllData(view)

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

    private fun getAllData(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            runData = MyRecordService().getAllRunData(UserInfo.userEmail.toString())
            CoroutineScope(Dispatchers.Main).launch {
                val recordAdapter = RecordListAdapter(runData)
                recordAdapter.notifyDataSetChanged()
                recordList.adapter = recordAdapter
                recordList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
                setCalender(view)
                getdDay(0)

            }
        }
    }

    private fun getdDay(day : Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse(runData[0].date.split("T")[0]).time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time
        val diff = ((today - startDate) / (24 * 60 * 60 * 1000)).toInt() - day
        when(diff){
            0 -> {dDay+=1}
            else -> {
                if(dDay ==0) runningTextView.text = "${diff}일 전이 마지막 운동이에요!!"
                else runningTextView.text = "${dDay} 연속 운동중이에요!!"
            }
        }
    }

    private fun getMyRecord() {
        CoroutineScope(Dispatchers.IO).launch {
            myRecord = MyRecordService().getMyRecord(UserInfo.userEmail)
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
                val goal = UserInfo.goalDistance/7
                runData.forEach{ item ->
                    if(data.date.compareTo(LocalDate.parse(item.date.split("T")[0], DateTimeFormatter.ISO_DATE))==0) {
                        if(item.date==lastDate){
                            tempDistance += item.distance
                        }
                        else {
                            tempDistance = item.distance
                            lastDate = item.date

                        }
                        if(tempDistance >= 0.7 * goal){
                            container.dayImageVIEW.setImageResource(emoticonResource[0])
                        }
                        else if(tempDistance >= 0.5 * goal) {
                            container.dayImageVIEW.setImageResource(emoticonResource[1])
                        }
                        else {
                            container.dayImageVIEW.setImageResource(emoticonResource[2])
                        }
                    }
                }
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