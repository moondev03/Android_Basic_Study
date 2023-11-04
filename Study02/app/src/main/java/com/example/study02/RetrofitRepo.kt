package com.example.study02

import android.content.Context
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RetrofitRepo {
    private var retrofit: Retrofit = RetrofitClient.getInstance()
    private var retrofitService: RetrofitService = retrofit.create(RetrofitService::class.java)

    val tag = "RETRO"

    fun getPosts(tvResult: TextView): TextView{

        retrofitService.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                Log.d(tag, response.body().toString())
                val res = response.body()
                tvResult.text = null

                if (res != null){
                    for (data in response.body()!!) {
                        tvResult.append("UserId: " + data.userId + "\n")
                        tvResult.append("Id: " + data.id + "\n")
                        tvResult.append("Title: " + data.title + "\n")
                        tvResult.append("Body: " + data.body + "\n\n")
                    }
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d(tag, t.message.toString())
            }
        })

        return tvResult
    }

    fun getPost(tvResult: TextView, id: Int): TextView{
        retrofitService.getPost(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.d(tag, response.body().toString())
                val res = response.body()
                tvResult.text = null

                if (res != null) {
                    tvResult.append("UserId: " + res.userId + "\n")
                    tvResult.append("Id: " + res.id + "\n")
                    tvResult.append("Title: " + res.title + "\n")
                    tvResult.append("Body: " + res.body + "\n\n")
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d(tag, t.message.toString())
            }
        })
        return tvResult
    }
}