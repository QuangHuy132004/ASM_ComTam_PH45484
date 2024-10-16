package com.example.asm_kot_ph45484

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Request

object RetrofitClient {

    var token: String? = null

    // Logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Custom OkHttpClient with token interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request: Request.Builder = chain.request().newBuilder()

            // Add token to header if not null
            val token = RetrofitClient.token
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(request.build())
        }
        .build()

    // Retrofit instance
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://comtam.phuocsangbn.workers.dev/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
