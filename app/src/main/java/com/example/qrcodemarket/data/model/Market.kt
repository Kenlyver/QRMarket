package com.example.qrcodemarket.data.model


data class Market(
    val marketId:String,
    val marketName:String,
    val qrCodeManagementId:String,
    val marketLocation:String,
    val imageQRCodeIn:String,
    val imageQRCodeOut:String
)
