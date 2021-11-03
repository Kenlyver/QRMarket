package com.example.qrcodemarket.data.model

data class dataMarketManage(val markets: ArrayList<MarketData>)
data class MarketData(
    val marketId:String,
    val marketName:String,
    val qrCodeManagementId:String,
    val marketLocation:String,
    val imageQRCodeIn:String,
    val imageQRCodeOut:String
)