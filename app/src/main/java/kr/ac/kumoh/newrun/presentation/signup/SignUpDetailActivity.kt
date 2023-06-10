package kr.ac.kumoh.newrun.presentation.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.ac.kumoh.newrun.databinding.ActivitySignUpDetailBinding
import kr.ac.kumoh.newrun.presentation.HomeActivity

class SignUpDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //자체 회원가입
        val userId = intent.getStringExtra("userId")
        val userPw = intent.getStringExtra("userPw")

        //소셜 로그인을 통해 얻은 회원가입
        val userEmail = intent.getStringExtra("userEmail")
        
        binding.btnSignUpComplete.setOnClickListener {
            val targetKm = binding.etKm.text
            val name = binding.etName.text
            val nickname = binding.etNickname.text

            Log.i("정상 회원가입 Id"," ${userId} ")
            Log.i("정상 회원가입 Pw"," ${userPw} ")

            Log.i("정상 회원가입 이름"," ${name} ")
            Log.i("정상 회원가입 별명"," ${nickname} ")
            Log.i("정상 회원가입 km"," ${targetKm} ")
            Log.i("정상 회원가입", "email: ${userEmail}")

            //통신 ㄱㄱ
            //if(id,pw 존재 시 -> 일반 회원가입)
            //else if(email 존재 시 -> )
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}