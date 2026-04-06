package com.example.food_retrofit.repository

import com.example.food_retrofit.data.model.Category.Category
import com.example.food_retrofit.data.model.Product.Product
import com.example.food_retrofit.data.remote.RetrofitClient

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import retrofit2.Call
import retrofit2.Response

class ProductRepository {

    fun getProducts(): Call<List<Product>> {
        return RetrofitClient.instance.getProducts()
    }
    suspend fun getProductsSuspend(): Response<List<Product>> {
        return RetrofitClient.instance.getProductsSuspend()
    }
    fun getCategories(): Call<List<Category>> {
        return RetrofitClient.instance.getCategories()
    }
    suspend fun getCategoriesSuspend(): Response<List<Category>> {
        return RetrofitClient.instance.getCategoriesSuspend()
    }
    fun deleteProducts(id: Int): Call<Void> {
        return RetrofitClient.instance.deleteProduct(id)
    }
    suspend fun deleteProductsSuspend(id: Int): Response<Void> {
        return RetrofitClient.instance.deleteProductSuspend(id)
    }
    fun addProducts(
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        categoryId: RequestBody,
        imagespart: List<MultipartBody.Part>
    ): Call<Product> {
        return RetrofitClient.instance.addProduct(
            title, subtitle, price, rating, categoryId, imagespart
        )
    }
    suspend fun addProductsSuspend(
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        categoryId: RequestBody,
        imagespart: List<MultipartBody.Part>
    ): Response<Product> {
        return RetrofitClient.instance.addProductSuspend(
            title, subtitle, price, rating, categoryId, imagespart
        )
    }
    fun updateProducts(
        id: Int,
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        category_id: RequestBody,
        imagespart: List<MultipartBody.Part>?
    ): Call<Product> {
        val method = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
        return RetrofitClient.instance.updateProduct(
            id,
            method,
            title,
            subtitle,
            price,
            rating,
            category_id,
            imagespart
        )
    }
    suspend fun updateProductsSuspend(
        id: Int,
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        category_id: RequestBody,
        imagespart: List<MultipartBody.Part>?
    ): Response<Product> {
        val method = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
        return RetrofitClient.instance.updateProductSuspend(
            id,
            method,
            title,
            subtitle,
            price,
            rating,
            category_id,
            imagespart
        )
    }
}