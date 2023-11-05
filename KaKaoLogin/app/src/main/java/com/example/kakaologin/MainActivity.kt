package com.example.kakaologin

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kakaologin.databinding.ActivityMainBinding
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyHash = Utility.getKeyHash(this)
        Log.e("Key","keyHash: $keyHash")

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("KAKAO", "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d("KAKAO", "사용자 정보 요청 성공 : $user")
                binding.tvNick.text = user.kakaoAccount?.profile?.nickname
            }
        }
    }
}

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }
}