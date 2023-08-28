package com.example.locationbasisnew

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpProvider {
     val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}