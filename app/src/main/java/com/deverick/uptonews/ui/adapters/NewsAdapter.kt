package com.deverick.uptonews.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deverick.uptonews.databinding.NewsItemBinding
import com.deverick.uptonews.models.News

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private lateinit var binding: NewsItemBinding

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((News) -> Unit)? = null

    fun setOnClickListener(listener: (News) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentNews = differ.currentList[position]

        binding.apply {
            newsTitle.text = currentNews.title
            newsDescription.text = currentNews.description
            newsDate.text = currentNews.date
            newsReadTime.text = currentNews.readTime
            Glide.with(holder.itemView).load(currentNews.image).into(newsImage)

            setOnClickListener {
                onItemClickListener?.let {
                    it(currentNews)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}