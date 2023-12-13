package com.example.dashboard


class Booking (val _id: String, val user: User, val flight: flight_items_view_model, val nbAdult: Int, val nbChildren: Int, val travelClass: String,
               var status:String?="en attente"
)
