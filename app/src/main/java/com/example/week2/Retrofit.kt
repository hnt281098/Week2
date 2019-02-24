package com.example.week2

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {
    private val base_url = "https://api.nytimes.com/svc/search/v2/"
    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}