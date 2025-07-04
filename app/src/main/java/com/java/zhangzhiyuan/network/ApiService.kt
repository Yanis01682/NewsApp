// 文件路径: app/src/main/java/com/java/你的名字/network/ApiService.kt
package com.java.zhangzhiyuan.network

import com.java.zhangzhiyuan.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Url // 导入 @Url 注解

interface ApiService {

    // 我们不再使用 @GET("相对路径") 和一堆 @Query
    // 而是直接使用 @GET 和 @Url 注解。
    // 这告诉Retrofit：“忽略BaseURL，直接请求这个@Url提供的完整地址”
    @GET
    suspend fun getNewsByFullUrl(@Url fullUrl: String): NewsResponse
}