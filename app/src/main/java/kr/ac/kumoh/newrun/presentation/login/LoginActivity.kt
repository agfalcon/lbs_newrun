package kr.ac.kumoh.newrun.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kr.ac.kumoh.newrun.presentation.HomeActivity
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.databinding.ActivityLoginBinding
import kr.ac.kumoh.newrun.presentation.signup.SignUpIDActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth//파이어베이스 객체 가져오기
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //<-------------------Kakao 로그인 로직--------------------->
        binding.btnKakao.setOnClickListener { kakaoSingIn() }
        binding.btnKakaologout.setOnClickListener { kakaoSingOut() }

        //<-------------------네이버 로그인 로직--------------------->
        //네이버아이디 객체 초기화
        //initialize(context: Context, clientId: String, clientSecret: String, clientName: String)
        NaverIdLoginSDK.initialize(this@LoginActivity, getString(R.string.naver_client_id), getString(
            R.string.naver_client_secret
        ), "테스트중이에요")
        binding.btnNaver.setOnClickListener {startNaverLogin() }
        binding.btnNaverlogout.setOnClickListener { startNaverLogout() }

        //<-------------------구글 로그인 로직---------------------
        binding.btnGoogle.setOnClickListener { signIn() }
        binding.btnGooglelogout.setOnClickListener { signOut() }
        //Google 로그인 옵션 구성. requestIdToken 및 Email 요청
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 파이어베이스 초기화
        auth = Firebase.auth

        //<-------------------자체 회원가입 로직---------------------
        binding.btnsignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpIDActivity::class.java)
            startActivity(intent)
        }
    }

    private fun kakaoSingOut() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                Toast.makeText(this@LoginActivity, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                Toast.makeText(this@LoginActivity, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun kakaoSingIn() {
        // callback => 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "call-back : 카카오계정으로 로그인 실패", error)
                Toast.makeText(this@LoginActivity, "카카오계정으로 로그인 실패", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Log.i(TAG, "call-back : 카카오계정으로 로그인 성공 ${token.accessToken}")
                Toast.makeText(this@LoginActivity, "카카오계정으로 로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        // 카카오톡이 설치 O =>  카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
    //onActivityResult => sub액티비티로 넘어갔다 다시 main으로 돌아올 때 사용되는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                binding.tvResult.text = "id: ${account.id} \n"
                firebaseAuthWithGoogle(account.idToken!!)
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // 로그인 성공 시 UI 업데이트
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser

            } else {
                // 로그인 실패 -> 로그 띄우기
                Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        Toast.makeText(this@LoginActivity, "구글 로그인 성공!", Toast.LENGTH_SHORT).show()
    }

    private fun signOut() {
        Firebase.auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            //updateUI(null)
        }
        Toast.makeText(this@LoginActivity, "구글 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


    //<----------------네이버 관련 로그인-------------->
    private fun startNaverLogin(){
        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                binding.tvResult.text = "id: ${userId} \ntoken: ${naverToken}"
                Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    private fun startNaverLogout(){
        NaverIdLoginSDK.logout()
        Toast.makeText(this@LoginActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }
}