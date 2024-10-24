package com.example.projekmap

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_article)

        // Ambil data dari Intent
        val articleTitle = intent.getStringExtra("ARTICLE_TITLE") ?: "Default Title"
        val articleContent = intent.getStringExtra("ARTICLE_CONTENT") ?: "Default Content"
        val articleDate = intent.getStringExtra("ARTICLE_DATE") ?: "Unknown Date"
        val articleImageRes = intent.getIntExtra("ARTICLE_IMAGE", R.drawable.yogyakarta)

        // Set data ke View
        findViewById<TextView>(R.id.article_title).text = articleTitle
        findViewById<TextView>(R.id.article_content).text = articleContent
        findViewById<TextView>(R.id.article_date).text = articleDate
        findViewById<ImageView>(R.id.article_author_image).setImageResource(articleImageRes)

        // Set back button functionality
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }
    }
}