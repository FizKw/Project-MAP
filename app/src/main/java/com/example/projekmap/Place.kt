package com.example.projekmap

import java.util.UUID

data class Place(
    val id: String,
    val vendor: String,
    val place: String,
    val avgRating: Double,
    val vendorImage: String,
    val desc: String, // Deskripsi tempat
    val estimate: String,
    val type: String,
    val via: String,
)
