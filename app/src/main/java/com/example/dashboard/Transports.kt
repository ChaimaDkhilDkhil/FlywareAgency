package com.example.dashboard

class Transports(val pays: String, val transport: Transport, val img: Int) {

    constructor(pays: String, img: Int, nbpersonne: Int) : this(pays, Transport("", "", "", "", 0, "", 0), img)
    // Assuming that you want to create Transports with the second constructor
// (pays, img, nbpersonne), you can modify the code as follows:

    val transportsInstance = Transports("France", R.drawable.transport, 5)

}
