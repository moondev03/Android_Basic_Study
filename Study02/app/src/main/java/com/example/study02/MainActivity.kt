package com.example.study02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.study02.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofitRepo: RetrofitRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofitRepo = RetrofitRepo()

        with(binding){
            btnPosts.setOnClickListener{
                tvResult.text = retrofitRepo.getPosts(tvResult).text
            }

            btnPost.setOnClickListener{
                if (etText.text != null){
                    val id = Integer.parseInt(etText.text.toString())
                    tvResult.text = retrofitRepo.getPost(tvResult, id).text
                }
            }
        }
    }
}