package com.example.food_retrofit.repository

import com.example.food_retrofit.data.model.Cart.CartRequest.CartRequest
import com.example.food_retrofit.data.model.Cart.CartResponse.CartResponse
import com.example.food_retrofit.data.remote.RetrofitClient

import retrofit2.Call
import retrofit2.Response

class CartRepository {

    // 🔹 ADD TO CART
    fun addToCart(request: CartRequest): Call<CartResponse> {
        return RetrofitClient.instance.addToCart(request)
    }

    suspend fun addToCartSuspend(request: CartRequest): Response<CartResponse> {
        return RetrofitClient.instance.addToCartSuspend(request)
    }

    // 🔹 GET CART
    fun getCart(userId: Int): Call<List<CartResponse>> {
        return RetrofitClient.instance.getCart(userId)
    }

    suspend fun getCartSuspend(userId: Int): Response<List<CartResponse>> {
        return RetrofitClient.instance.getCartSuspend(userId)
    }

    // 🔹 UPDATE CART
    fun updateCart(id: Int, request: CartRequest): Call<CartResponse> {
        return RetrofitClient.instance.updateCart(id, request)
    }

    suspend fun updateCartSuspend(id: Int, request: CartRequest): Response<CartResponse> {
        return RetrofitClient.instance.updateCartSuspend(id, request)
    }

    // 🔹 DELETE CART
    fun deleteCart(id: Int): Call<Void> {
        return RetrofitClient.instance.deleteCart(id)
    }

    suspend fun deleteCartSuspend(id: Int): Response<Void> {
        return RetrofitClient.instance.deleteCartSuspend(id)
    }

    // 🔹 CLEAR CART
    fun clearCart(userId: Int): Call<Void> {
        return RetrofitClient.instance.clearCart(userId)
    }

    suspend fun clearCartSuspend(userId: Int): Response<Void> {
        return RetrofitClient.instance.clearCartSuspend(userId)
    }
}