package com.example.food_retrofit.data.model.Cart.CartRequest

data class CartRequest (
    val user_id : Int,
    val product_id : Int,
    val quantity : Int
)