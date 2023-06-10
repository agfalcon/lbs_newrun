package kr.ac.kumoh.newrun.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.databinding.ActivityLoginBinding
import kr.ac.kumoh.newrun.databinding.ActivityLoginNractivityBinding
import kr.ac.kumoh.newrun.presentation.signup.SignUpIDActivity

class LoginNRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginNractivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginNractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpIDActivity::class.java)
            startActivity(intent)
        }
    }
}