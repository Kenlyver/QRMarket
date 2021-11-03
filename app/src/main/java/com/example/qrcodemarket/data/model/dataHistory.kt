package com.example.qrcodemarket.data.model


data class dataHistory(val access: ArrayList<HistoryData>)
data class HistoryData(
    val citizenId: String,
    val marketName: String,
    val timeIn: String,
    val timeOut: String,
    val date: String,
    val location: String
)