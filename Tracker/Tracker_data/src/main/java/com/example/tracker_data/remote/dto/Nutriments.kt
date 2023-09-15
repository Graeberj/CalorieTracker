package com.example.tracker_data.remote.dto

import com.squareup.moshi.Json

data class Nutriments(
    @field:Json(name = "carbohydrates_100g")
    val carbohydrates100g: Double,
    @field:Json(name = "energy-kcal_100g")
    val energyKcal100g: Double,
    @field:Json(name = "fat_100g")
    val fat100g: Double,
    @field:Json(name = "protein_100g")
    val protein100g: Double,
)
