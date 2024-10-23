package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePageFragment : BaseAuthFragment() {

    private lateinit var popularNearbyRecyclerView: RecyclerView
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var nearestLocationRecyclerView: RecyclerView
    private lateinit var chooseLocationRecyclerView: RecyclerView
    private lateinit var articleRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        // Initialize RecyclerViews
        popularNearbyRecyclerView = view.findViewById(R.id.popularNearbyRecyclerView)
        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView)
        nearestLocationRecyclerView = view.findViewById(R.id.nearestLocationRecyclerView)
        chooseLocationRecyclerView = view.findViewById(R.id.chooseLocationRecyclerView)
        articleRecyclerView = view.findViewById(R.id.articleRecyclerView) // Initialize article RecyclerView

        // Setup RecyclerViews
        setupPopularNearbyRecyclerView()
        setupRecommendedRecyclerView()
        setupNearestLocationRecyclerView()
        setupChooseLocationRecyclerView()
        setupArticleRecyclerView() // Add setup for Article

        return view
    }

    private fun setupPopularNearbyRecyclerView() {
        val popularNearbyPlaces = listOf(
            Place("Semeru Mountain", "East Java, Indonesia", 4.5, R.drawable.semeru, "Semeru Mountain is the highest volcano in Java."),
            Place("Raja Ampat", "West Papua, Indonesia", 4.7, R.drawable.raja_ampat, "Raja Ampat is famous for its stunning marine biodiversity."),
            Place("Bali Island", "Bali, Indonesia", 4.8, R.drawable.bali, "Bali is a popular tourist destination known for its beaches and temples.")
        )
        val adapter = PopularNearbyAdapter(popularNearbyPlaces)
        popularNearbyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popularNearbyRecyclerView.adapter = adapter
    }

    private fun setupRecommendedRecyclerView() {
        val recommendedPlaces = listOf(
            Place("Kelimutu Mountain", "Flores, NTT", 4.3, R.drawable.kelimutu, "Kelimutu is famous for its three colored lakes."),
            Place("Karimun Jawa Island", "Jepara, Central Java", 4.6, R.drawable.karimunjawa, "Karimunjawa is a tropical paradise with clear water."),
            Place("Pahawang Island", "Lampung, Indonesia", 4.5, R.drawable.pahawang, "Pahawang is a hidden gem with pristine beaches.")
        )
        val adapter = RecommendedAdapter(recommendedPlaces)
        recommendedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recommendedRecyclerView.adapter = adapter
    }

    private fun setupNearestLocationRecyclerView() {
        val nearestPlaces = listOf(
            Place("Pramuka Island", "Thousand Island, Jakarta Kep", 4.5, R.drawable.pramuka_island, "Pramuka Island is famous for its pristine beaches."),
            Place("Pari Island", "Thousand Island, Jakarta Kep", 4.3, R.drawable.pari_island, "Pari Island is a beautiful island with clear waters and great snorkeling spots.")
        )
        val adapter = NearestLocationAdapter(nearestPlaces)
        nearestLocationRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        nearestLocationRecyclerView.adapter = adapter
    }

    private fun setupChooseLocationRecyclerView() {
        val locations = listOf(
            Place("Central Java", "Yogyakarta, Indonesia", 0.0, R.drawable.central_java, "Explore cultural heritage in Central Java."),
            Place("Yogyakarta", "Yogyakarta, Indonesia", 0.0, R.drawable.yogyakarta, "Discover the beauty of Yogyakarta.")
        )
        val adapter = ChooseLocationAdapter(locations)
        chooseLocationRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        chooseLocationRecyclerView.adapter = adapter
    }

    // Setup for Article RecyclerView
    private fun setupArticleRecyclerView() {
        val articles = listOf(
            Article("Discover Yogyakarta", "Jan 2023", R.drawable.yogyakarta),
            Article("Cultural Gems of Central Java", "Feb 2023", R.drawable.central_java)
        )

        // Set adapter dengan listener untuk navigasi ke fragment detail
        val adapter = ArticleAdapter(articles) { article ->
            // Ketika artikel diklik, navigasi ke ArticleDetailFragment
            val articleFragment = ArticleFragment.newInstance(
                article.title,
                article.date,
                article.imageResId
            )

            // Lakukan fragment transaction untuk mengganti fragment saat ini dengan ArticleDetailFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, articleDetailFragment) // Pastikan Anda mengganti R.id.fragment_container dengan ID dari container di activity
                .addToBackStack(null) // Menambahkan fragment ke backstack agar user bisa kembali
                .commit()
        }

        articleRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        articleRecyclerView.adapter = adapter
    }

}
