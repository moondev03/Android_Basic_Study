package com.example.naver_news_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.naver_news_app.databinding.ActivityMainBinding
import com.example.naver_news_app.model.ArticleDAO
import com.example.naver_news_app.model.ArticleEntity
import com.example.naver_news_app.model.Item
import com.example.naver_news_app.model.RetrofitClient
import com.example.naver_news_app.model.RetrofitService
import com.example.naver_news_app.model.RoomDB
import com.example.naver_news_app.model.SearchResult
import com.example.naver_news_app.view.SearchItemAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var retrofitClient: Retrofit
    private lateinit var retrofitService: RetrofitService
    private lateinit var binding: ActivityMainBinding
    private val clientId = BuildConfig.NAVER_CLIENT_ID
    private val clientSecret = BuildConfig.NAVER_CLIENT_SECRET
    private var db: RoomDB? = null
    private var items: List<Item>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.test = this
        setContentView(binding.root)

        db = RoomDB.getInstance(this)
        val articleDao: ArticleDAO? = db?.articleDao()

        retrofitClient = RetrofitClient.getInstance()
        retrofitService = retrofitClient.create(RetrofitService::class.java)

        binding.rvResult.layoutManager = LinearLayoutManager(this)
//        val adapter = SearchItemAdapter(emptyList())
        val adapter = SearchItemAdapter(db)
        binding.rvResult.adapter = adapter

        thread {
            items = articleDao?.getAll()
        }

        if(items != null) { adapter.setItems() }


        binding.run {
            btnPosts.setOnClickListener{
                thread {
                    retrofitService.getSearchNews(clientId, clientSecret, etText.text.toString(), 30)
                        .enqueue(object: Callback<SearchResult> {
                            override fun onResponse(
                                call: Call<SearchResult>,
                                response: Response<SearchResult>
                            ) {
                                Log.d("Result", "성공: ${response.raw()}")
                                if (response.isSuccessful) {
                                    val searchResult = response.body()
                                    val items = searchResult?.items
                                    if (items != null) {
                                        val itemEntities = items.map {
                                            ArticleEntity(
                                                title = it.title,
                                                originalLink = it.originallink,
                                                link = it.link,
                                                description = it.description,
                                                pubDate = it.pubDate
                                            )
                                        }

                                        thread {
                                            articleDao?.insertAll(itemEntities)
                                        }

                                        runOnUiThread{
                                            adapter.setItems()
                                        }
                                    }

                                }
                            }

                            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                                Log.d("Result", "실패: $t")
                            }
                        })
                }

                etText.text = null
            }
        }
    }
}