package com.example.kakaologinnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kakaologinnew.databinding.ActivityMainBinding
import com.kakao.sdk.user.UserApiClient
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        // 현재 로그인한 사용자의 정보를 불러온다
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(TAG, "사용자 정보 요청 성공 : $user")
                _binding.tvNickname.text = user.kakaoAccount?.profile?.nickname
                GlideApp.with(this)
                    .load(user.kakaoAccount?.profile?.profileImageUrl)
                    .override(400, 400)
                    .circleCrop()
                    .into(_binding.ivProfile)
            }
        }

        // Logout or Unlink를 중복 요청하는 경우를 막기 위해 Dialog를 띄워 막음
        dialog = AlertDialog.Builder(this)
            .setMessage("진행 중...")
            .setCancelable(false)
            .create()

        // 사용자 액세스 토큰과 리프레시 토큰을 모두 만료시켜, 더 이상 해당 사용자 정보로 카카오 API를 호출할 수 없도록 한다
        // 로그아웃은 요청 성공 여부와 관계없이 토큰을 삭제 처리한다는 점에 유의
        _binding.btnLogout.setOnClickListener{
            dialog.show()
            UserApiClient.instance.logout { error ->
                dialog.dismiss()
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        // 카카오 플랫폼 안에서 앱과 사용자 카카오계정의 연결 상태를 해제한다(회원탈퇴의 의미)
        // 연결이 끊어지면 기존의 토큰은 더 이상 사용할 수 없으므로, 연결 끊기 요청 성공 시 로그아웃 처리가 함께 이뤄져 토큰이 삭제된다
        _binding.btnUnlink.setOnClickListener{
            dialog.show()
            UserApiClient.instance.unlink { error ->
                dialog.dismiss()
                if (error != null) {
                    Log.e(TAG, "연결 끊기 실패", error)
                }
                else {
                    Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
}