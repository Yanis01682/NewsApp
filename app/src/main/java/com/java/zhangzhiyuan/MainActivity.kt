// 文件路径: app/src/main/java/com/java/你的名字/MainActivity.kt
package com.java.zhangzhiyuan // 确保包名正确

// 导入所有需要的“工具包”
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.java.zhangzhiyuan.R
import com.java.zhangzhiyuan.ui.NewsAdapter
import com.java.zhangzhiyuan.ui.NewsViewModel

class MainActivity : AppCompatActivity() {

    // 声明ViewModel和Adapter变量，我们稍后会初始化它们
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    // 这是Activity创建时会执行的第一个函数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. 设置当前Activity要使用的布局文件
        setContentView(R.layout.activity_main)

        // 2. 初始化界面上的控件和数据
        setupRecyclerView()
        setupViewModel()

        // 3. 命令ViewModel去获取新闻数据
        // 因为我们把固定的URL放到了ViewModel里，所以这里调用时不再需要任何参数
        viewModel.fetchNews()
    }

    // 一个专门用来设置RecyclerView的函数，让onCreate更整洁
    private fun setupRecyclerView() {
        // 通过ID找到布局文件里的RecyclerView控件
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // 设置布局管理器，它决定了新闻是上下垂直滚动
        recyclerView.layoutManager = LinearLayoutManager(this)
        // 创建Adapter实例，先给它一个空列表
        newsAdapter = NewsAdapter(emptyList())
        // 将Adapter和RecyclerView关联起来
        recyclerView.adapter = newsAdapter
    }

    // 一个专门用来设置ViewModel的函数
    private fun setupViewModel() {
        // 获取ViewModel的实例
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        // **设置监听**：告诉Activity，“请一直盯着ViewModel里的newsList数据”
        viewModel.newsList.observe(this) { news ->
            // 一旦newsList的数据发生变化（比如网络请求成功），这个括号里的代码就会执行
            // 我们在这里更新Adapter的数据，让新新闻显示出来
            newsAdapter.updateData(news)
        }

        // **设置另一个监听**：告诉Activity，“请一直盯着ViewModel里的error数据”
        viewModel.error.observe(this) { errorMessage ->
            // 一旦error数据发生变化（比如网络请求失败），这个代码就会执行
            // 我们用一个Toast把错误信息弹出来，告诉用户发生了什么
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}