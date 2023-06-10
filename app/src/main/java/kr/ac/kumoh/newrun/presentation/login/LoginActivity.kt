package kr.ac.kumoh.newrun.presentation.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kumoh.newrun.presentation.HomeActivity
import kr.ac.kumoh.newrun.R
import kr.ac.kumoh.newrun.data.repository.UserService
import kr.ac.kumoh.newrun.databinding.ActivityLoginBinding
import kr.ac.kumoh.newrun.domain.data.UserInfo
import kr.ac.kumoh.newrun.presentation.signup.SignUpDetailActivity
import kr.ac.kumoh.newrun.presentation.signup.SignUpIDActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth//파이어베이스 객체 가져오기
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        kakao_autoLogin()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //<-------------------Kakao 로그인 로직--------------------->
        binding.btnKakao.setOnClickListener { kakaoSingIn() }
        binding.btnKakaologout.setOnClickListener { kakaoSingOut() }


        //<-------------------네이버 로그인 로직--------------------->
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

        //테스트용 !!!!--- 나중에 지울거
        getUserInfo()
        naver_autoLogin()
    }
    private fun naver_autoLogin() {
        var naverToken :String? = ""
        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                Log.i(TAG, "넘길 네이버 정보 \n" +
                        "token: ${naverToken} \n" +
                        " ${response.profile?.name }\n" +
                        " ${response.profile?.email}\n")
                Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@LoginActivity, SignUpDetailActivity::class.java)
                intent.putExtra("userEmail", response.profile?.email.toString())
                startActivity(intent)
                //finish()
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

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                Log.i(TAG, "네이버 : 성공")
                naverToken = NaverIdLoginSDK.getAccessToken()
                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
                var naverTokenType = NaverIdLoginSDK.getTokenType()
                var naverState = NaverIdLoginSDK.getState().toString()
                Log.i(TAG, "네이버 : 성공${naverToken}")

                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                Log.i(TAG, "네이버 : 실패")
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                Log.i(TAG, "네이버 : 에러")
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    //카톡 자동 로그인
    private fun kakao_autoLogin() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                //에러 존재
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) { //로그인 필요
                    } else { //기타 에러
                    }
                }
                //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                else {
                    Log.i(TAG, "카톡 : 토큰 유효성 체크 성공(필요 시 토큰 갱신됨)")
                    //토큰 요청
                    UserApiClient.instance.me { user, error ->
                            val email = user?.kakaoAccount?.email
//                            Log.i(
//                                TAG, "사용자 정보 요청 성공" +
//                                        "\n회원번호: ${user.id}"
//                                        //"\n이메일: ${user.kakaoAccount?.email}" +
//                                        //"\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
//                                        //"\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
//                            )
                            val intent = Intent(this@LoginActivity, SignUpDetailActivity::class.java)
                            intent.putExtra("userEmail", email.toString())
                            startActivity(intent)
                            finish()
                    }
                }
            }
        }
        else { //로그인 필요}
        }
    }

    private fun kakaoSingOut() {
        UserApiClient.instance.unlink { error ->
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
                Log.e(TAG, "카톡이 읎네")
                Log.e(TAG, "call-back : 카카오계정으로 로그인 실패", error)
                Toast.makeText(this@LoginActivity, "카카오계정으로 로그인 실패", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Log.e(TAG, "카톡이 읎네")
                Log.i(TAG, "call-back : 카카오계정으로 로그인 성공 ${token.accessToken}")
                Toast.makeText(this@LoginActivity, "카카오계정으로 로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        Log.e(TAG, "카카오톡 로그인 시도")
        // 카카오톡이 설치 O =>  카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            Log.e(TAG, "카카오톡 설치되있네요")
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
                }
                else if (token != null) {
                    Log.e(TAG, "카카오톡 토큰!")
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        }
        else {
            Log.e(TAG, "카톡이 없으시네요")
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카톡없음 : 에러 string이 차있다.")
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.e(TAG, "카톡없음 : 토큰생성.")
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                }
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
        Log.i(TAG, "구글 로그인 시도")
        val acct = GoogleSignIn.getLastSignedInAccount(application)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl
            Log.i(TAG, "구글 정보 읽기 성공 : $personName $personEmail $personId")
        } else {
            Log.i(TAG, "구글 정보 읽기 실패")
            Log.i(TAG, "구글 정보 읽기 실패 ${acct}")
        }

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
        private const val TAG = "테스트"
        private const val RC_SIGN_IN = 9001
    }


    //<----------------네이버 관련 로그인-------------->
    private fun startNaverLogin(){
        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                Log.i(TAG, "넘길 네이버 정보 \n" +
                        "token: ${naverToken} \n" +
                        " ${response.profile?.name }\n" +
                        " ${response.profile?.email}\n")
                Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@LoginActivity, SignUpDetailActivity::class.java)
                intent.putExtra("userEmail", response.profile?.email.toString())
                startActivity(intent)
                //finish()
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

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
                var naverTokenType = NaverIdLoginSDK.getTokenType()
                var naverState = NaverIdLoginSDK.getState().toString()

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
        NaverIdLoginSDK.authenticate(application, oauthLoginCallback)
    }

    private fun startNaverLogout(){
        NaverIdLoginSDK.logout()
        Toast.makeText(this@LoginActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }

    private fun getUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            UserService().getUserInfo(UserInfo.userEmail)
        }
    }
}


//유배 코드
////onActivityResult => sub액티비티로 넘어갔다 다시 main으로 돌아올 때 사용되는 메소드
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                binding.tvResult.text = "id: ${account.id} \n"
//                firebaseAuthWithGoogle(account.idToken!!)
//                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
//                startActivity(intent)
//
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//            }
//        }
//    }
