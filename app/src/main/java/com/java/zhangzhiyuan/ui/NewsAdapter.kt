// 文件路径: app/src/main/java/com/java/你的名字/ui/NewsAdapter.kt
package com.java.zhangzhiyuan.ui

import android.content.Intent
import android.util.Log // 确保导入Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.java.zhangzhiyuan.NewsDetailActivity
import com.java.zhangzhiyuan.R
import com.java.zhangzhiyuan.model.NewsItem
import org.json.JSONArray

class NewsAdapter(private var newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.news_title)
        val publisher: TextView = itemView.findViewById(R.id.news_publisher)
        val time: TextView = itemView.findViewById(R.id.news_publish_time)
        val image: ImageView = itemView.findViewById(R.id.news_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.title.text = newsItem.title
        holder.publisher.text = newsItem.publisher
        holder.time.text = newsItem.publishTime

        // --- 图片加载的详细逻辑 ---
        try {
            // 在Logcat里用 "ImageDebug" 作为标签，方便我们过滤查找
            Log.d("ImageDebug", "原始image字段内容: ${newsItem.image}")

            // 接口返回的image字段是一个JSON数组格式的字符串，比如 "[]" 或 "[\"url1\"]"
            val jsonArray = JSONArray(newsItem.image)
            if (jsonArray.length() > 0) {
                // 获取数组中的第一个图片URL
                val imageUrl = jsonArray.getString(0)
                Log.d("ImageDebug", "成功解析出URL: $imageUrl")

                // 检查URL是否为空或不是一个有效的http(s)链接
                if (imageUrl.isNullOrBlank() || !imageUrl.startsWith("http")) {
                    Log.w("ImageDebug", "URL无效，使用占位图。")
                    holder.image.setImageResource(R.drawable.ic_launcher_background)
                } else {
                    // 使用Glide库加载图片
                    Glide.with(holder.itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background) // 加载中的占位图
                        .error(R.drawable.ic_launcher_background)       // 加载失败时显示的图
                        .into(holder.image)
                }
            } else {
                // 如果JSON数组为空 "[]"
                Log.d("ImageDebug", "图片数组为空，使用占位图。")
                holder.image.setImageResource(R.drawable.ic_launcher_background)
            }
        } catch (e: Exception) {
            // 如果解析JSON出错，说明image字段格式有问题
            Log.e("ImageDebug", "解析图片JSON失败，使用占位图。", e)
            holder.image.setImageResource(R.drawable.ic_launcher_background)
        }

        // 点击事件的逻辑保持不变
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra("news_title", newsItem.title)
            intent.putExtra("news_content", newsItem.content)
            intent.putExtra("news_source", newsItem.publisher)
            intent.putExtra("news_time", newsItem.publishTime)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateData(newNewsList: List<NewsItem>) {
        this.newsList = newNewsList
        notifyDataSetChanged()
    }
}