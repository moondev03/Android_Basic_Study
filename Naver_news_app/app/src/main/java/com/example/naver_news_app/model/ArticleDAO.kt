package com.example.naver_news_app.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(items: List<ArticleEntity>)

//    @Insert
//    fun insertArticle(vararg item: Item)

    @Query("SELECT * FROM Article")
    fun getAll(): List<Item>
}