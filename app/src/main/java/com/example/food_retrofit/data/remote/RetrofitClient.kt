package com.example.food_retrofit.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //private const val BASE_URL = "http://10.0.2.2:8000/api/"
    private const val BASE_URL = "https://foodapplaravel-production.up.railway.app/api/"
//    private const val BASE_URL = "https://nonfaulty-bethel-stringed.ngrok-free.dev/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}