package kr.ac.kumoh.newrun.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.domain.data.RankData

class RankAdapter(private val context: Context, private val rankData: List<RankData>): BaseAdapter() {

    override fun getCount(): Int =rankData.size

    override fun getItem(index: Int): RankData = rankData[index]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.rank_layout, null)

        val rankImage = view.findViewById<ImageView>(R.id.rankImageView)
        val rankText = view.findViewById<TextView>(R.id.rankTextView)
        val nameText = view.findViewById<TextView>(R.id.nameTextView)
        val distance = view.findViewById<TextView>(R.id.distanceTextView)
        val distanceUnit = view.findViewById<TextView>(R.id.distanceUnitTextView)

        val imageResource = when(position) {
            0 -> getDrawable(context, R.drawable.gold_medal)
            1 -> getDrawable(context, R.drawable.silver_medal)
            2 -> getDrawable(context, R.drawable.dong_medal)
            else -> null
        }

        rankText.text = if(position<3) "" else (position + 1).toString()
        nameText.text = rankData[position].name
        distance.text = rankData[position].distance.toString()
        distanceUnit.text = "km"
        rankImage.setImageDrawable(imageResource)

        return view
    }
}