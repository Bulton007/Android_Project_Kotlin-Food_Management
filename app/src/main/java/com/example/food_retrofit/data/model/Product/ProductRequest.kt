package com.example.food_retrofit.data.model.Product

data class ProductRequest(
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val category_id: Int,
    val images : List<String>
)