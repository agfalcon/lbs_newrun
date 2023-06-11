package kr.ac.kumoh.newrun.presentation.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.domain.data.VisitPathData

class PathAdapter(val context: Context, val PathList: ArrayList<VisitPathData>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.path_layout, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val pathImg = view.findViewById<ImageView>(R.id.pathImg)
        val pathDate = view.findViewById<TextView>(R.id.pathDate)
        val pathTime = view.findViewById<TextView>(R.id.pathTime)
        val pathDistance = view.findViewById<TextView>(R.id.distanceTextView)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val path = PathList[position]
//        val imageResource = when(position) {
//            0 -> AppCompatResources.getDrawable(context, R.drawable.gold_medal)
//
//        val resource = context.resources.getIdentifier(path.photo, "drawable", context.packageName)
        //val imageRes = AppCompatResources.getDrawable(context, R.drawable.gold_medal)
        pathImg.setImageBitmap(stringToBitmap(path.photo))
        pathDate.text = path.date
        pathTime.text = path.time
        pathDistance.text = path.distance.toString()
        return view
    }

    override fun getItem(position: Int): Any {
        return PathList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return PathList.size
    }

    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}