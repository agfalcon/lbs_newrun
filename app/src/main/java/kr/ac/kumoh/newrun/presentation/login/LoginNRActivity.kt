package kr.ac.kumoh.newrun.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.model.LoginData
import kr.ac.kumoh.newrun.data.repository.LoginService
import kr.ac.kumoh.newrun.data.repository.UserService
import kr.ac.kumoh.newrun.databinding.ActivityLoginBinding
import kr.ac.kumoh.newrun.databinding.ActivityLoginNractivityBinding
import kr.ac.kumoh.newrun.domain.data.UserInfo
import kr.ac.kumoh.newrun.presentation.HomeActivity
import kr.ac.kumoh.newrun.presentation.signup.SignUpIDActivity

class LoginNRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginNractivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginNractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼
        binding.btnLogin.setOnClickListener {
            if(binding.etId.text.toString().length <= 4){
                Toast.makeText(applicationContext, "아이디는 5자 이상입니다!", Toast.LENGTH_SHORT).show()
            } else if(binding.etPw.text.toString().length <= 3) {
                Toast.makeText(applicationContext, "비밀번호는 4자 이상입니다!", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(
                    Dispatchers.IO
                ).launch {
                    val result = LoginService().login(
                        LoginData(
                            email = binding.etId.text.toString(),
                            password = binding.etPw.text.toString()
                        )
                    )

                    //abcd@naver.com
                    //1234
                    Log.i("테스트", result)
                    Log.i("테스트", binding.etId.text.toString())
                    Log.i("테스트", binding.etPw.text.toString())

                    if (result == "로그인성공") {
                        //이미 회원가입 => 바로 메인 페이지 ㄱㄱ
                        CoroutineScope(
                            Dispatchers.IO
                        ).launch {
                            UserInfo.userEmail = binding.etId.text.toString()
                            getUserInfo()

                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                        }
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
        
        //회원가입 버튼
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpIDActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            UserService().getUserInfo(UserInfo.userEmail)
        }
    }
}