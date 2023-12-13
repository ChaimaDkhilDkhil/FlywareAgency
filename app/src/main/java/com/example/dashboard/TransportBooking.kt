package com.example.dashboard

import java.util.Date

data class TransportBooking(
    val _id: String?,
    val name: String,
    val pays: String,
    val title: String,
    val location: String,
    val price: String,
    val description: String,
    val nbPersonne: Int,
    val date: String,
    val luggage: Int,
    var status:String?="en attente"
)
