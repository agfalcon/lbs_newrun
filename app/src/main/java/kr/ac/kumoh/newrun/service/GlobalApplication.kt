package kr.ac.kumoh.newrun.service
import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.view.NidOAuthLoginButton
import com.navercorp.nid.oauth.view.NidOAuthLoginButton.Companion.oauthLoginCallback
import kr.ac.kumoh.newrun.R

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "b43a2fb428ca6657385740c6f50ed547")
        NaverIdLoginSDK.initialize(this,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            "테스트중이에요")
    }
}