package kr.ac.kumoh.newrun.presentation.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.domain.model.RecordListItem

class RecordListAdapter(val itemList: List<RecordListItem>) :
    RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_list_item, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.time.text = itemList[position].time
        holder.velocity.text = itemList[position].velocity
        holder.distance.text = itemList[position].distance
        holder.dDAY.text = itemList[position].dDay
        holder.date.text = itemList[position].date
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