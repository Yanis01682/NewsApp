// 文件路径: app/src/main/java/com/java/你的名字/ui/NewsViewModel.kt
package com.java.zhangzhiyuan.ui

import android.util.Log // 导入Log类，方便我们看日志
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.java.zhangzhiyuan.model.NewsItem
import com.java.zhangzhiyuan.network.RetrofitClient
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> = _newsList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // 定义老师在文档里提供的那个完整、不变的URL
    private val teacherProvidedUrl = "https://api2.newsminer.net/svc/news/queryNewsList?size=15&startDate=2024-06-20&endDate=2024-08-30&words=拜登&categories=科技&page=1"

    fun fetchNews() {
        viewModelScope.launch {
            try {
                // 打印出我们要请求的URL，方便在Logcat里核对
                Log.d("NewsViewModel", "Requesting URL: $teacherProvidedUrl")

                // 调用新的接口，直接传入完整的URL字符串
                val response = RetrofitClient.apiService.getNewsByFullUrl(teacherProvidedUrl)

                if (response.data.isNullOrEmpty()) {
                    _error.postValue("网络请求成功，但API返回了空数据列表！")
                } else {
                    _newsList.postValue(response.data)
                }

            } catch (e: Exception) {
                // 如果还失败，打印出详细的错误信息
                Log.e("NewsViewModel", "Network request failed", e)
                _error.postValue("网络请求失败: ${e.message}")
            }
        }
    }
}