package com.example.qrcodemarket.data.respositories

import com.example.qrcodemarket.data.network.SyntheticApi
import com.example.qrcodemarket.data.network.response.SafeApiRequest

class MarketRepository(
    private val api: SyntheticApi
) : SafeApiRequest() {
    suspend fun getMarket() = apiRequest { api.dataMarket() }
}