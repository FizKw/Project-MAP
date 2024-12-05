package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class ArticleEditFragment : Fragment() {

    private lateinit var articleId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleId = it.getString("articleId", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article_edit, container, false)

        // Back button
        view.findViewById<View>(R.id.back_button).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Save button
        view.findViewById<View>(R.id.btnSave).setOnClickListener {
            Snackbar.make(view, "Article saved!", Snackbar.LENGTH_SHORT).show()
            // Implement save logic here
        }

        return view
    }
}
