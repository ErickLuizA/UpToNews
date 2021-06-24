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
            return oldItem.url == newItem.url
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

        val title =
            if (currentNews.title!!.length > 70) "${
                currentNews.title.subSequence(
                    0,
                    70
                )
            }..." else currentNews.title

        val description =
            if (currentNews.description!!.length > 200) "${
                currentNews.description.subSequence(
                    0,
                    200
                )
            }..." else currentNews.description

        val author =
            if (currentNews.author!!.length > 30) "${
                currentNews.author.subSequence(
                    0,
                    30
                )
            }..." else currentNews.author

        val date = currentNews.published!!.subSequence(0, 10)

        binding.apply {
            newsTitle.text = title
            newsDescription.text = description
            newsAuthor.text = author
            newsDate.text = date
            Glide.with(holder.itemView).load(currentNews.image).into(newsImage)

            newsItemLayout.setOnClickListener {
                onItemClickListener?.let {
                    it(currentNews)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}