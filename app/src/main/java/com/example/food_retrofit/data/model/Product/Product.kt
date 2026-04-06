package com.example.food_retrofit.data.model.Product
import com.example.food_retrofit.data.model.Category.Category
import com.example.food_retrofit.data.model.ProductImage.ProductImage
import java.io.Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val images: List<ProductImage>,
    val category: Category
) : Serializable