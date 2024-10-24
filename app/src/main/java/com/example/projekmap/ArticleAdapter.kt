package com.example.projekmap


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Article(
    val title: String,  // Judul artikel
    val content: String,
    val date: String,   // Tanggal artikel
    val imageRes: Int // ID resource untuk gambar
)

// Adapter untuk mengelola artikel
class ArticleAdapter(private val articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)

        // Pindah ke detail artikel saat diklik
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra("ARTICLE_TITLE", article.title)
                putExtra("ARTICLE_CONTENT", article.content)
                putExtra("ARTICLE_DATE", article.date)
                putExtra("ARTICLE_IMAGE", article.imageRes)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {
            itemView.findViewById<TextView>(R.id.article_title).text = article.title
            itemView.findViewById<TextView>(R.id.article_date).text = article.date
            itemView.findViewById<ImageView>(R.id.article_image).setImageResource(article.imageRes)
        }
    }
}