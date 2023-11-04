package com.example.naver_news_app


/*
JSON은
{}: JSON Object를 의미 -> name - value 형태
[]: JSON Array를 의미 -> Index
*/

data class SearchResult(
    var lastBuildDate: String,
    var total: Int,
    var start: Int,
    var display: Int,
    var items: List<Item>
)

data class Item(
    var title: String,
    var originallink: String,
    var link: String,
    var description: String,
    var pubDate: String
)