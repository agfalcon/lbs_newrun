package kr.ac.kumoh.newrun.presentation.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.repository.StableDiffusionService




class ImageFragment : Fragment() {
    private lateinit var imageButton : Button
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageButton = view.findViewById(R.id.getImageButton)
        imageView = view.findViewById(R.id.imageView)
        imageButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val result = StableDiffusionService().getImage()
                Log.d("테스트", result.toString())
                val image = stringToBitmap(result.image)
                CoroutineScope(Dispatchers.Main).launch {
                    imageView.setImageBitmap(image)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)

    }

    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }

}