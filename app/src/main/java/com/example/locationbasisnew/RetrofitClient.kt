package com.example.locationbasisnew

import com.example.locationbasisnew.OkHttpProvider.okHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    const val BASE_URL = "https://api.geoapify.com/v1/geocode/"
    const val API_KEY = "e9c9c1fe1f7b472ca207f70645786f32"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.okHttpClient)
        .build()

    val locationInterface: LocationInterface = retrofit.create(LocationInterface::class.java)


}