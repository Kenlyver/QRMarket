package com.example.qrcodemarket.data.repositories

import com.example.qrcodemarket.data.network.MyApi
import com.example.qrcodemarket.data.network.response.AuthResponse
import com.example.qrcodemarket.data.network.response.SafeApiRequest

class UserRepository(
    private val api:MyApi
):SafeApiRequest() {

    suspend fun userLogin(loginName:String,password:String): AuthResponse? {
        return apiRequest { api.userLogin(loginName,password)}
    }

}