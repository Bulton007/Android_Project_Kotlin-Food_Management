package com.example.food_retrofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.food_retrofit.data.model.Cart.CartRequest.CartRequest
import com.example.food_retrofit.data.model.Cart.CartResponse.CartResponse
import com.example.food_retrofit.repository.CartRepository

import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    // 🔹 ADD TO CART
    fun addToCart(request: CartRequest): Call<CartResponse> {
        return repository.addToCart(request)
    }

    fun addToCartSuspend(request: CartRequest, onResult: (Response<CartResponse>) -> Unit) {
        viewModelScope.launch {
            val res = repository.addToCartSuspend(request)
            onResult(res)
        }
    }

    // 🔹 GET CART
    fun getCart(userId: Int): Call<List<CartResponse>> {
        return repository.getCart(userId)
    }

    fun getCartSuspend(userId: Int, onResult: (Response<List<CartResponse>>) -> Unit) {
        viewModelScope.launch {
            val res = repository.getCartSuspend(userId)
            onResult(res)
        }
    }

    // 🔹 UPDATE CART
    fun updateCart(id: Int, request: CartRequest): Call<CartResponse> {
        return repository.updateCart(id, request)
    }

    fun updateCartSuspend(id: Int, request: CartRequest, onResult: (Response<CartResponse>) -> Unit) {
        viewModelScope.launch {
            val res = repository.updateCartSuspend(id, request)
            onResult(res)
        }
    }

    // 🔹 DELETE CART
    fun deleteCart(id: Int): Call<Void> {
        return repository.deleteCart(id)
    }

    fun deleteCartSuspend(id: Int, onResult: (Response<Void>) -> Unit) {
        viewModelScope.launch {
            val res = repository.deleteCartSuspend(id)
            onResult(res)
        }
    }

    // 🔹 CLEAR CART
    fun clearCart(userId: Int): Call<Void> {
        return repository.clearCart(userId)
    }

    fun clearCartSuspend(userId: Int, onResult: (Response<Void>) -> Unit) {
        viewModelScope.launch {
            val res = repository.clearCartSuspend(userId)
            onResult(res)
        }
    }
}