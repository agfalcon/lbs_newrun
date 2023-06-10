package kr.ac.kumoh.newrun.presentation.image

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kr.ac.kumoh.newrun.databinding.DialogCreateImageBinding

class ImageDialogFragment: DialogFragment() {
    private var _binding: DialogCreateImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogCreateImageBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.imageButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onResume() {

        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}