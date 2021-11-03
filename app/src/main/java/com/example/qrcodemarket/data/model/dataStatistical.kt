package com.example.qrcodemarket.data.model

data class dataStatistical(val access: ArrayList<StatisticalData>)
data class StatisticalData(
    val citizenId: String, val fullName: String,
    val marketId: String, val marketName: String,
    val timeIn: String, val timeOut: String, val date: String
)