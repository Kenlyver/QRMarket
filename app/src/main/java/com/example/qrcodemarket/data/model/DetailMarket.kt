package com.example.qrcodemarket.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DetailMarket(
    val marketId:String = "",
    val marketName:String ="",
    val qrCodeManagementId:String ="",
    val marketLocation:String ="",
    val imageQRCodeIn:String ="",
    val imageQRCodeOut:String =""
): Parcelable