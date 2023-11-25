package com.example.dashboard

class Hotels(val pays: String, val hotel: Hotel, val img: Int) {
    constructor(pays: String, img: Int) : this(pays, Hotel("","","",""), img)
}