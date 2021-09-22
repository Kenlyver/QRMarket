package com.example.qrcodemarket.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DetailUser(
    val fullName:String = "",
    val dateOfBirth:String = "",
    val numberPhone:String = "",
    val loginName:String = "",
    val password:String = ""
): Parcelable