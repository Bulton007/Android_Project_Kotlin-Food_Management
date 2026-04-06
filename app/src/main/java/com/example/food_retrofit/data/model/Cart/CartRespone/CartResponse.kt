package com.example.food_retrofit.data.model.Cart.CartResponse

import com.example.food_retrofit.data.model.Product.Product

data class CartResponse(
    val id: Int,
    val user_id: Int,
    val product_id: Int,
    var quantity: Int,
    val product: Product
)