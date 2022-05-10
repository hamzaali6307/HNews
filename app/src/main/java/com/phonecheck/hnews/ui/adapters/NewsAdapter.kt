package com.phonecheck.hnews.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.phonecheck.hnews.R
import com.phonecheck.hnews.databinding.ItemArticlePreviewBinding
import com.phonecheck.hnews.ui.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter(private val onItemClick: (article: Article) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binding(article: Article) {

            itemView.apply {
                ivArticleImage.load(article.urlToImage) {
                    crossfade(true)
                    error(R.drawable.ic_error)
                    placeholder(R.drawable.ic_loading)
                }
                tvSource.text = article.source?.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt
                setOnClickListener {
                    onItemClick(article)
                }
            }
        }
    }
}

