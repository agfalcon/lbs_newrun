package kr.ac.kumoh.newrun.presentation.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import kr.ac.kumoh.newrun.presentation.HomeActivity
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.databinding.ActivitySignUpPwBinding

class SignUpPwActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpPwBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUpComplete.alpha = 0.5f
        binding.check7more.alpha = 0.5f
        binding.chekcPwEqual.alpha = 0.5f
        val passwordInput: EditText = findViewById(R.id.text_input_signup_pw)
        val passwordCheckInput: EditText = findViewById(R.id.text_input_signup_pw_check)

        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = passwordInput.text.toString()
                val passwordCheck = passwordCheckInput.text.toString()
                updateUI(password, passwordCheck)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        passwordCheckInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = passwordInput.text.toString()
                val passwordCheck = passwordCheckInput.text.toString()
                updateUI(password, passwordCheck)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        //<-----------회원가입 완료(다이어로그)--------------->
        binding.btnSignUpComplete.setOnClickListener {
            showDialog()
        }
    }
    private fun updateUI(password: String, passwordCheck: String) {
        if (password.length >= 7) {
            binding.check7more.alpha = 1.0f
            binding.check7more.text = "■ 8자 이상"

            if (password == passwordCheck) {
                // 비밀번호와 비밀번호 확인이 일치하는 경우
                binding.chekcPwEqual.alpha = 1.0f
                binding.chekcPwEqual.text = "■ 비밀번호 일치"
                binding.btnSignUpComplete.alpha = 1.0f
            } else {
                binding.chekcPwEqual.text = "□ 비밀번호 일치"
                binding.chekcPwEqual.alpha = 0.5f
                binding.btnSignUpComplete.alpha = 0.5f
                if (password.length < 8) {
                    binding.check7more.alpha = 0.5f
                    binding.check7more.text = "□ 8자 이상"
                } else {
                    binding.check7more.alpha = 1.0f
                    binding.check7more.text = "■ 8자 이상"
                }
            }
        }
    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater

        // 다이얼로그 뷰 초기화
        val dialogView = inflater.inflate(R.layout.dialog_suc_signup, null)

        // btn_success_signup 버튼에 클릭 이벤트 리스너 등록
        dialogView.findViewById<Button>(R.id.btn_success_signup).setOnClickListener {
            // Intent를 사용하여 다른 화면으로 이동
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setOnShowListener {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            val dialogWidth = (width * 0.85).toInt() // 디바이스 가로 크기의 60%
            val dialogHeight = (height * 0.55).toInt() // 디바이스 세로 크기의 80%
            alertDialog.window?.setLayout(dialogWidth, dialogHeight)
        }

        alertDialog.show()
    }
}