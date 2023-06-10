package kr.ac.kumoh.newrun.presentation.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kr.ac.kumoh.newrun.databinding.ActivitySignUpIdBinding

class SignUpIDActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpIdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.alpha = 0.5f
        binding.textInputLogin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) >= 5) {
                    binding.btnNext.alpha = 1.0f
                } else {
                    binding.btnNext.alpha = 0.5f
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnNext.setOnClickListener {
            val userId = binding.textInputLogin.text.toString()
            val intent = Intent(this, SignUpPwActivity::class.java)
            intent.putExtra("userId", userId)
            Log.i("ID페이지 : "," ${userId} ")
            startActivity(intent)
        }
    }
}