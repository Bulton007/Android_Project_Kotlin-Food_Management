package com.example.food_retrofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.food_retrofit.data.model.Category.Category
import com.example.food_retrofit.data.model.Product.Product
import com.example.food_retrofit.repository.ProductRepository

import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    // 🔹 GET PRODUCTS
    fun getProducts(): Call<List<Product>> {
        return repository.getProducts()
    }

    fun getProductsSuspend(onResult: (Response<List<Product>>) -> Unit) {
        viewModelScope.launch {
            val res = repository.getProductsSuspend()
            onResult(res)
        }
    }

    // 🔹 GET CATEGORIES
    fun getCategories(): Call<List<Category>> {
        return repository.getCategories()
    }

    fun getCategoriesSuspend(onResult: (Response<List<Category>>) -> Unit) {
        viewModelScope.launch {
            val res = repository.getCategoriesSuspend()
            onResult(res)
        }
    }

    // 🔹 DELETE PRODUCT
    fun deleteProduct(id: Int): Call<Void> {
        return repository.deleteProducts(id)
    }

    fun deleteProductSuspend(id: Int, onResult: (Response<Void>) -> Unit) {
        viewModelScope.launch {
            val res = repository.deleteProductsSuspend(id)
            onResult(res)
        }
    }

    // 🔹 ADD PRODUCT
    fun addProducts(
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        categoryId: RequestBody,
        imagespart: List<MultipartBody.Part>
    ): Call<Product> {
        return repository.addProducts(title, subtitle, price, rating, categoryId, imagespart)
    }

    fun addProductsSuspend(
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        categoryId: RequestBody,
        imagespart: List<MultipartBody.Part>,
        onResult: (Response<Product>) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.addProductsSuspend(
                title, subtitle, price, rating, categoryId, imagespart
            )
            onResult(res)
        }
    }

    // 🔹 UPDATE PRODUCT
    fun updateProducts(
        id: Int,
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        category_id: RequestBody,
        imagespart: List<MultipartBody.Part>?
    ): Call<Product> {
        return repository.updateProducts(
            id,
            title,
            subtitle,
            price,
            rating,
            category_id,
            imagespart
        )
    }

    fun updateProductsSuspend(
        id: Int,
        title: RequestBody,
        subtitle: RequestBody,
        price: RequestBody,
        rating: RequestBody,
        category_id: RequestBody,
        imagespart: List<MultipartBody.Part>?,
        onResult: (Response<Product>) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.updateProductsSuspend(
                id,
                title,
                subtitle,
                price,
                rating,
                category_id,
                imagespart
            )
            onResult(res)
        }
    }
}