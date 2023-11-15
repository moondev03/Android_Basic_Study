package com.example.kakaologinnew

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaologinnew.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityLoginBinding

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
            nextActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.btnKakao.setOnClickListener{
            doLogin()
        }


    }

    private fun doLogin(){
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

                    UserApiClient.instance.me { user, error ->
                        if(error != null){
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        }
                        else if(user != null){
                            val scopes = mutableListOf<String>()

                            if (user.kakaoAccount?.emailNeedsAgreement == true) { scopes.add("account_email") }
                            if (user.kakaoAccount?.birthdayNeedsAgreement == true) { scopes.add("birthday") }
                            if (user.kakaoAccount?.birthyearNeedsAgreement == true) { scopes.add("birthyear") }
                            if (user.kakaoAccount?.genderNeedsAgreement == true) { scopes.add("gender") }
                            if (user.kakaoAccount?.phoneNumberNeedsAgreement == true) { scopes.add("phone_number") }
                            if (user.kakaoAccount?.profileNeedsAgreement == true) { scopes.add("profile") }
                            if (user.kakaoAccount?.ageRangeNeedsAgreement == true) { scopes.add("age_range") }
                            if (user.kakaoAccount?.ciNeedsAgreement == true) { scopes.add("account_ci") }

                            if(scopes.isNotEmpty()){
                                Log.d(TAG, "사용자에게 추가 동의를 받아야 합니다")
                            }

                            nextActivity()
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun nextActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}