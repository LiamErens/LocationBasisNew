package com.example.locationbasisnew

import retrofit2.http.GET
import retrofit2.http.Query


interface LocationInterface {
    @GET("reverse")
    fun getAddress(
        @Query("apikey") apiKey: String,
        @Query("ts") timeStamp: String
    )
}