package com.example.qrcodemarket.data.network.response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {
    companion object{

        val BASE_URL = "http://192.168.1.2:80/myapi/public/"

        fun getRetroInstance():Retrofit{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}