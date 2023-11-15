package com.example.kakaologinnew

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility


// Application Class는 앱 실행 시 우선적으로 인스턴스화됨.
class GlobalApplication: Application() {

    override fun onCreate(){
        super.onCreate()
        getKeyHash()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }

    private fun getKeyHash(){
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
    }
}