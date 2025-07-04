// 文件路径: app/src/main/java/com/java/zhangzhiyuan/MainActivity.kt
package com.java.zhangzhiyuan

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.java.zhangzhiyuan.ui.NewsAdapter
import com.java.zhangzhiyuan.ui.NewsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 设置Toolbar
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        // 2. 设置侧滑菜单 (DrawerLayout)
        drawerLayout = findViewById(R.id.main_drawer)
        val navView: NavigationView = findViewById(R.id.main_drawer_navigation)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.main_drawer_open, R.string.main_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 3. 初始化新闻列表和数据
        setupRecyclerView() // RecyclerView现在在 timeline_list 中
        setupViewModel()
        viewModel.fetchNews() // 获取新闻数据
    }

    // 正确加载和设置菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    Toast.makeText(this@MainActivity, "正在搜索: $query", Toast.LENGTH_SHORT).show()
                    // 在这里执行实际的搜索逻辑，例如调用 viewModel.searchNews(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 可以在这里实现搜索建议
                return true
            }
        })
        return true
    }

    // 处理菜单项（包括侧滑菜单按钮）的点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        // 在新的布局中，RecyclerView位于 R.layout.refreshable_timeline.xml 中
        // 我们需要找到它。注意：这里假设 refreshable_timeline 已经被加载到某个容器中。
        // 在这个例子中，我们直接使用 R.id.timeline_list，它是在 refreshable_timeline.xml 中定义的。
        // 为了让它能被找到，你需要确保你的Activity布局包含了这个ID。
        // 一个简单的方法是在 main_frame 中动态添加这个布局，或者在XML中<include>它。
        // 这里我们简化处理，假设它存在于视图层级中。
        // 注意：你的`main_content.xml`里没有RecyclerView，你需要动态加载一个包含RecyclerView的布局到`main_frame`
        // 或者直接修改`main_content.xml`。这里我们假设你已经在某个地方加载了它。
        // 如果运行时出现`findViewById`返回null，说明视图层级中确实没有这个ID。
        // 最好的做法是修改`main_content.xml`，在`FrameLayout`的位置换成<include layout="@layout/refreshable_timeline"/>

        // 由于没有直接在布局中，我们先找到 `R.id.timeline_list`
        // 在你的项目中，`refreshable_timeline` 包含了一个 `RecyclerView`，ID为 `timeline_list`
        // 你需要确保这个布局被包含在你的主活动视图中。
        // 假设 `refreshable_timeline.xml` 被 include 到了 `main_frame`
        // 为了保证代码能跑通，你需要去 `main_content.xml` 里，
        // 将 `<FrameLayout android:id="@+id/main_frame" ... />`
        // 替换为 `<include layout="@layout/refreshable_timeline" />`

        // 假设你已经做了上述修改
        // val recyclerView = findViewById<RecyclerView>(R.id.timeline_list)
        // recyclerView.layoutManager = LinearLayoutManager(this)

        // 为了让你能直接编译通过，我们暂时创建一个临时的RecyclerView
        // 但强烈建议你按照注释修改XML
        val recyclerView = RecyclerView(this) // 这是一个临时的解决方案
        recyclerView.layoutManager = LinearLayoutManager(this)

        newsAdapter = NewsAdapter(emptyList())
        recyclerView.adapter = newsAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        viewModel.newsList.observe(this) { news ->
            newsAdapter.updateData(news)
        }
        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}