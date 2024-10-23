package com.example.projekmap


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Article(
    val title: String,  // Judul artikel
    val date: String,   // Tanggal artikel
    val imageResId: Int // ID resource untuk gambar
)

// Adapter untuk mengelola artikel
class ArticleAdapter(
    private val articles: List<Article>,
    private val onItemClick: (Article) -> Unit // Menambahkan callback untuk item click
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    // ViewHolder yang memegang referensi ke item layout (item_article.xml)
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.article_image)
        val articleTitle: TextView = itemView.findViewById(R.id.article_title)
        val articleDate: TextView = itemView.findViewById(R.id.article_date)
    }

    // Membuat view holder baru saat RecyclerView memerlukan item baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        // Inflate layout item_article.xml untuk setiap item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    // Mengikat data ke ViewHolder berdasarkan posisi item di list
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        // Ambil data artikel berdasarkan posisi
        val article = articles[position]

        // Set data artikel ke dalam views di ViewHolder
        holder.articleTitle.text = article.title
        holder.articleDate.text = article.date
        holder.articleImage.setImageResource(article.imageResId) // Menggunakan resource ID gambar
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }

        // Jika Anda ingin memuat gambar dari URL, gunakan Glide atau Picasso:
        // Glide.with(holder.itemView.context).load(article.imageUrl).into(holder.articleImage)
    }

    // Menyatakan jumlah item di RecyclerView
    override fun getItemCount(): Int {
        return articles.size
    }
}
