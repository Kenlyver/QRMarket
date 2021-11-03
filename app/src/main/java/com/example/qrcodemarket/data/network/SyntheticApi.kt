package com.example.qrcodemarket.data.network


import com.example.qrcodemarket.data.model_new.Market
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET



interface SyntheticApi {


    @GET("allmarkets")
    suspend fun dataMarket(): Response<List<Market>>


//        companion object {
//        operator fun invoke(): SyntheticApi {
//            return Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("http://192.168.1.8:80/myapi/public/")
//                .build()
//                .create(SyntheticApi::class.java)
//        }
//    }
    companion object {
    var service: SyntheticApi? = null
        fun getInstance(): SyntheticApi? {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.2:80/myapi/public/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                service = retrofit.create(SyntheticApi::class.java)
            }
            return service
        }

    }
}