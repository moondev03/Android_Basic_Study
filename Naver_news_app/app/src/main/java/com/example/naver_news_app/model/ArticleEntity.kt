package com.example.naver_news_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Article")
data class ArticleEntity(
    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "originallink")
    val originalLink: String,

    @PrimaryKey
    @ColumnInfo(name = "link")
    val link: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "pubDate")
    val pubDate: String
)

