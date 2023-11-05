package com.example.kakaologin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.kakaologin.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        Log.d("KAKAO", "keyhasy : ${Utility.getKeyHash(this)}")

        setContentView(binding.root)
        binding.btnLogin.setOnClickListener(this)
    }

    private var mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if(error != null){
            Log.e("KAKAO", "로그인 실패 $error")
        } else if(token != null){
            Log.d("KAKAO", "로그인 성공 ${token.accessToken}")
            nextMainActivity()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogin.id -> {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                    UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                        if (error != null) {
                            Log.e("KAKAO", "로그인 실패 $error")
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            } else {
                                UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                            }
                        } else if (token != null) {
                            Log.d("KAKAO", "로그인 성공 ${token.accessToken}")
                            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                            nextMainActivity()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                }
            }
        }

    }
    private fun nextMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}