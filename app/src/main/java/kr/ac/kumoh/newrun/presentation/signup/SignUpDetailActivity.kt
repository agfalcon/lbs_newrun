package kr.ac.kumoh.newrun.presentation.signup

import android.content.Intent
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.data.model.LoginData
import kr.ac.kumoh.newrun.data.model.SignUp
import kr.ac.kumoh.newrun.data.repository.LoginService
import kr.ac.kumoh.newrun.data.repository.SignUpService
import kr.ac.kumoh.newrun.databinding.ActivitySignUpDetailBinding
import kr.ac.kumoh.newrun.domain.data.UserInfo
import kr.ac.kumoh.newrun.presentation.HomeActivity
import kr.ac.kumoh.newrun.presentation.login.LoginNRActivity

class SignUpDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //자체 회원가입
        val userId = intent.getStringExtra("userId").toString()
        val userPw = intent.getStringExtra("userPw").toString()

        //소셜 로그인을 통해 얻은 회원가입
        val userEmail = intent.getStringExtra("userEmail").toString()

        binding.btnSignUpComplete.setOnClickListener {
            val targetKm = binding.etKm.text.toString()
            val name = binding.etName.text.toString()
            val nickname = binding.etNickname.text.toString()

            Log.i("정상 회원가입 Id"," ${userId} ")
            Log.i("정상 회원가입 Pw"," ${userPw} ")
            Log.i("정상 회원가입 이름"," ${name} ")
            Log.i("정상 회원가입 별명"," ${nickname} ")
            Log.i("정상 회원가입 km"," ${targetKm} ")
            Log.i("정상 회원가입", "email: ${userEmail}")

            Log.i("테스트","로그인 진입")
            if(userEmail != "null"){
                //소셜 서버 통신 -> 용구가 나중에 만들면
                Log.i("테스트","소셜 로그인 수행")

            }
            else {
                //자체 로그인
                Log.i("테스트","자체 로그인 수행")
                CoroutineScope(
                    Dispatchers.IO
                ).launch {
                    val result = SignUpService().signUp(
                        SignUp(
                            email = userId,
                            userName = name,
                            nickName = nickname,
                            password = userPw,
                            goal_Distance = targetKm.toFloat(),
                        )
                    )
                    Log.i("테스트",result)
                    if (result == "가입성공") {
                        val intent = Intent(applicationContext, LoginNRActivity::class.java)
                        startActivity(intent)
                    } else {
                        CoroutineScope(
                            Dispatchers.Main
                        ).launch {
                            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
