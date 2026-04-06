package com.example.food_retrofit.data.remote

import com.example.food_retrofit.data.model.Cart.CartRequest.CartRequest
import com.example.food_retrofit.data.model.Cart.CartResponse.CartResponse
import com.example.food_retrofit.data.model.Product.Product
import com.example.food_retrofit.data.model.Category.Category

import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Response

import retrofit2.http.*

interface ApiService {
    @GET("categories")
    fun getCategories(): Call<List<Category>>
    @GET("categories")
    suspend fun getCategoriesSuspend(): Response<List<Category>>
    @GET("cart")
    fun getCart(@Query("user_id") userId: Int): Call<List<CartResponse>>
    @GET("cart")
    suspend fun getCartSuspend(@Query("user_id") userId: Int): Response<List<CartResponse>>
    @POST("cart")
    fun addToCart(@Body request: CartRequest): Call<CartResponse>
    @POST("cart")
    suspend fun addToCartSuspend(@Body request: CartRequest): Response<CartResponse>
    @PUT("cart/{id}")
    fun updateCart(
        @Path("id") id: Int,
        @Body request: CartRequest
    ): Call<CartResponse>
    @PUT("cart/{id}")
    suspend fun updateCartSuspend(
        @Path("id") id: Int,
        @Body request: CartRequest
    ): Response<CartResponse>
    @DELETE("cart/{id}")
    fun deleteCart(@Path("id") id: Int): Call<Void>
    @DELETE("cart/{id}")
    suspend fun deleteCartSuspend(@Path("id") id: Int): Response<Void>
    @DELETE("cart/clear/{user_id}")
    fun clearCart(@Path("user_id") userId: Int): Call<Void>
    @DELETE("cart/clear/{user_id}")
    suspend fun clearCartSuspend(@Path("user_id") userId: Int): Response<Void>
    @GET("products")
    fun getProducts(): Call<List<Product>>
    @GET("products")
    suspend fun getProductsSuspend(): Response<List<Product>>
    @Multipart
    @POST("products")
    fun addProduct(
        @Part("title") title: RequestBody,
        @Part("subtitle") subtitle: RequestBody,
        @Part("price") price: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<Product>
    @Multipart
    @POST("products")
    suspend fun addProductSuspend(
        @Part("title") title: RequestBody,
        @Part("subtitle") subtitle: RequestBody,
        @Part("price") price: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<Product>
    @Multipart
    @POST("products/{id}")
    fun updateProduct(
        @Path("id") id: Int,
        @Part("_method") method: RequestBody,
        @Part("title") title: RequestBody,
        @Part("subtitle") subtitle: RequestBody,
        @Part("price") price: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): Call<Product>
    @Multipart
    @POST("products/{id}")
    suspend fun updateProductSuspend(
        @Path("id") id: Int,
        @Part("_method") method: RequestBody,
        @Part("title") title: RequestBody,
        @Part("subtitle") subtitle: RequestBody,
        @Part("price") price: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): Response<Product>
    @DELETE("products/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<Void>
    @DELETE("products/{id}")
    suspend fun deleteProductSuspend(@Path("id") id: Int): Response<Void>
}