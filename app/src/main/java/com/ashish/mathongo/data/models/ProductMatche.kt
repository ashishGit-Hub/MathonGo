package com.ashish.mathongo.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductMatche(
    val averageRating: Double,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val link: String,
    val price: String,
    val ratingCount: Double,
    val score: Double,
    val title: String
): Parcelable