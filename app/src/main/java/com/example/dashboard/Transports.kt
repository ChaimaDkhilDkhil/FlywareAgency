package com.example.dashboard

class Transports(val pays: String, val transport: Transport, val img: Int) {
    constructor (pays: String, img: Int) : this(pays, Transport("", "", "", "", ), img)
}
