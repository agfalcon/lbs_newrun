package kr.ac.kumoh.newrun.presentation.image

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.repository.StableDiffusionService
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.animation.content.Content
import com.kakao.sdk.common.util.SdkLogLevel
import kr.ac.kumoh.newrun.data.model.RunData
import kr.ac.kumoh.newrun.data.repository.ImageService
import kr.ac.kumoh.newrun.data.repository.MyRecordService
import kr.ac.kumoh.newrun.domain.data.UserInfo
import kr.ac.kumoh.newrun.domain.data.VisitPathData


class ImageFragment : Fragment() {
    private lateinit var PathListView: ListView

    private lateinit var resultData : List<RunData>

    var PathList = arrayListOf<VisitPathData>(
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.IO).launch {
            resultData = MyRecordService().getAllRunData(UserInfo.userEmail)
            resultData.forEach {
                val image = ImageService().getImage(it.id)
                PathList.add(VisitPathData(it.date.split("T")[0], it.runTime, it.distance, image))
            }
            PathListView = view.findViewById(R.id.PathListView)
            val PathAdapter = PathAdapter(requireContext(), PathList)
            CoroutineScope(Dispatchers.Main).launch {
                PathListView.adapter = PathAdapter
                PathAdapter.notifyDataSetChanged()
                PathListView.setOnItemClickListener { parent, view, position, id ->
                    val data = resultData[position]
                    val intent = Intent(requireActivity(), ImageDetailActivity::class.java)
                    intent.putExtra("resultData", data)
                    startActivity(intent)
                }
            }
        }





        super.onViewCreated(view, savedInstanceState)

    }



}
