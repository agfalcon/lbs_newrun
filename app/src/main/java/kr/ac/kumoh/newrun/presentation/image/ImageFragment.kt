package kr.ac.kumoh.newrun.presentation.image

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
import kr.ac.kumoh.newrun.domain.data.VisitPathData


class ImageFragment : Fragment() {
    private lateinit var PathListView: ListView

    var PathList = arrayListOf<VisitPathData>(
        VisitPathData("2023-06-01", "03시간 3분 기록", "sample"),
        VisitPathData("2023-06-02", "13시간 3분 기록", "sample"),
        VisitPathData("2023-06-03", "23시간 3분 기록", "sample"),
        VisitPathData("2023-06-01", "03시간 3분 기록", "sample"),
        VisitPathData("2023-06-02", "13시간 3분 기록", "sample"),
        VisitPathData("2023-06-03", "23시간 3분 기록", "sample"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        return view
    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = layoutInflater

        // 다이얼로그 뷰 초기화
        val dialogView = inflater.inflate(R.layout.dialog_suc_signup, null)

        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setOnShowListener {
//                val displayMetrics = DisplayMetrics()
//                //windowManager.defaultDisplay.getMetrics(displayMetrics)
//                val width = displayMetrics.widthPixels
//                val height = displayMetrics.heightPixels
//                val dialogWidth = (width * 0.85).toInt() // 디바이스 가로 크기의 60%
//                val dialogHeight = (height * 0.55).toInt() // 디바이스 세로 크기의 80%
//                alertDialog.window?.setLayout(dialogWidth, dialogHeight)

        }

        //  alertDialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        PathListView = view.findViewById(R.id.PathListView)


        val PathAdapter = PathAdapter(requireContext(), PathList)
        PathListView.adapter = PathAdapter

        PathListView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "${position + 1} 번째 클릭했슴다", Toast.LENGTH_SHORT)
                .show()
            //showDialog()
            val dialog = ImageDialogFragment()
            dialog.show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        view.findViewById<TextView>(R.id.createTitleTextView).setOnClickListener {
            showDialog()
        }
        super.onViewCreated(view, savedInstanceState)

    }

    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }

}
