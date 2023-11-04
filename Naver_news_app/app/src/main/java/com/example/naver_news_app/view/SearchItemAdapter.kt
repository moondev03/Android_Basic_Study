package com.example.naver_news_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchItemAdapter(private var items: List<Item>) : RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

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
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvLink.text = item.link
        holder.tvDescription.text = item.description
        holder.tvPubData.text = item.pubDate
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}
