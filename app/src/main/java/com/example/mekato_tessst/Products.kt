package com.example.mekato_tessst

import android.media.Rating



data class Products(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating

)
