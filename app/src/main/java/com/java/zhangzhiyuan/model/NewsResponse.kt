package com.java.zhangzhiyuan.model
// 文件路径: app/src/main/java/com/java/你的名字/model/NewsResponse.kt


data class NewsResponse(
    val data: List<NewsItem>,
    val pageSize: Int,
    val total: Int,
    val currentPage: Int
)

data class NewsItem(
    val newsID: String,
    val title: String,
    val publisher: String,
    val publishTime: String,
    val image: String,
    val content: String,
    val video: String
    // 你可以根据 response_example.json 添加更多需要的字段
)