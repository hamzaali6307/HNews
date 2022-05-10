package com.phonecheck.hnews.apiCall

import com.phonecheck.hnews.apiCall.interfaces.NewsApi
import com.phonecheck.hnews.utills.Constant.URL_BASE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {

    companion object{
        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        
        val api: NewsApi by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }
}