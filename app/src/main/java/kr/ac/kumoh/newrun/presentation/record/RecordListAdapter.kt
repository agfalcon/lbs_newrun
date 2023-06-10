package kr.ac.kumoh.newrun.presentation.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.model.RunData
import kr.ac.kumoh.newrun.domain.model.RecordListItem
import java.text.SimpleDateFormat
import java.util.Calendar

class RecordListAdapter(val itemList: List<RunData>) :
    RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_list_item, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.time.text = if(itemList[position].runTime.substring(0,2)=="00") itemList[position].runTime.substring(3) else itemList[position].runTime
        holder.velocity.text = itemList[position].speed.toString() + "km/h"
        holder.distance.text = itemList[position].distance.toString() + "km"
        val date = itemList[position].date.split("T")[0]
        holder.date.text = date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = dateFormat.parse(date).time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time
        val diff = ((today - startDate) / (24 * 60 * 60 * 1000)).toInt()
        holder.dDAY.text = when(diff){
            0 -> "오늘"
            1 -> "하루 전"
            2 -> "이틀 전"
            else -> "${diff}일 전"
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val canvas = itemView.findViewById<View>(R.id.routeCanvas)
        val dDAY = itemView.findViewById<TextView>(R.id.dDayTextView)
        val date = itemView.findViewById<TextView>(R.id.dateTextView)
        val distance = itemView.findViewById<TextView>(R.id.distanceTextView)
        val velocity = itemView.findViewById<TextView>(R.id.velocityTextView)
        val time = itemView.findViewById<TextView>(R.id.timeTextView)
    }
}