package kr.ac.kumoh.newrun.presentation.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.model.RunData
import kr.ac.kumoh.newrun.data.repository.ImageService
import kr.ac.kumoh.newrun.data.repository.StableDiffusionService
import kr.ac.kumoh.newrun.databinding.ActivityImageDetailBinding

class ImageDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageDetailBinding
    var imageRoute = ""
    var isSave = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<RunData>("resultData")
        CoroutineScope(Dispatchers.IO).launch {
            imageRoute = ImageService().getImage(data?.id ?: 0)
            CoroutineScope(Dispatchers.Main).launch {
                binding.pathImg.setImageBitmap(stringToBitmap(imageRoute))
            }
        }
        binding.imageButton.setOnClickListener {
            if(!isSave){
                CoroutineScope(Dispatchers.IO).launch {
                    val result = StableDiffusionService().getImage(binding.textInputLogin.text.toString(), imageRoute)
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.resultImage.setImageBitmap(stringToBitmap(result.image))
                        binding.imageButton.text = "이미지 저장"
                        isSave = true
                    }
                }
            } else {

            }
        }
    }

    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}