package com.example.naver_news_app.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.naver_news_app.model.Item
import com.example.naver_news_app.R
import com.example.naver_news_app.model.ArticleDAO
import com.example.naver_news_app.model.RoomDB
import kotlin.concurrent.thread

class SearchItemAdapter(db: RoomDB?) : RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {
    private val articleDAO = db?.articleDao()
    private var items: List<Item> = emptyList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvLink: TextView = itemView.findViewById(R.id.tv_link)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvPubData: TextView = itemView.findViewById(R.id.tv_pubData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]

        thread{
            items = articleDAO!!.getAll()
        }

        if(items != null){
            val item = items!![position]

            holder.tvTitle.text = item.title
            holder.tvLink.text = item.link
            holder.tvDescription.text = item.description
            holder.tvPubData.text = item.pubDate
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems() {
        thread {
            items = articleDAO!!.getAll()
        }
        notifyDataSetChanged()
    }
}
