package com.example.qrcodemarket.data.model

import com.google.gson.annotations.SerializedName

object getMarket {
    data class Response(
        @SerializedName("error") val error: Boolean,
        @SerializedName("markets") val data: List<Data> = listOf()
    )

    data class Data(
        @SerializedName("marketId") val marketId:String = "",
        @SerializedName("marketName") val marketName:String = "",
        @SerializedName("qrCodeManagementId") val qrCodeManagementId:String = "",
        @SerializedName("marketLocation") val marketLocation:String = "",
        @SerializedName("imageQRCodeIn") val imageQRCodeIn:String = "",
        @SerializedName("imageQRCodeOut") val imageQRCodeOut:String = ""
    )
}