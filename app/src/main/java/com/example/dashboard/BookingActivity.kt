package com.example.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class BookingActivity : AppCompatActivity() {
    private lateinit var nbAdulte :EditText
    private lateinit var nbChildren :EditText
    private lateinit var btnbook :Button
    private var requestQueue: RequestQueue? = null
    lateinit var duration:String
    lateinit var date:String
    lateinit var returnDate:String
    lateinit var destination:String
    lateinit var departure:String
    var price:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        val classSpinner: Spinner = findViewById(R.id.classSpinner)

        val classes = arrayOf("Business", "Economy")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        classSpinner.adapter = adapter
        duration = intent.getStringExtra("duration").toString()
        date = intent.getStringExtra("date").toString()
        returnDate = intent.getStringExtra("returnDate").toString()
        destination = intent.getStringExtra("destination").toString()
        departure = intent.getStringExtra("departure").toString()
        price = intent.getDoubleExtra("price",0.0)
        requestQueue = Volley.newRequestQueue(this)
        findViewById<TextView>(R.id.destinationAdd).setText(destination)
        findViewById<TextView>(R.id.dateAdd).setText  (date)
        findViewById<TextView>(R.id.durationAdd).setText (duration)
        findViewById<TextView>(R.id.returnDateAdd).setText  (returnDate)
        findViewById<TextView>(R.id.departureAdd).setText (departure)
        findViewById<TextView>(R.id.priceAdd).setText (price.toString())
        nbChildren=findViewById(R.id.nbChildren)
        nbAdulte=findViewById(R.id.nbAdult)
        btnbook=findViewById(R.id.addbtn)
        btnbook.setOnClickListener {
            val url = "http://192.168.56.1:3000/bookings"
            val jsonObject = JSONObject().apply {
                put("duration", duration)
                put("date", date)
                put("returnDate", returnDate)
                put("destination", destination)
                put("departure", departure)
                put("price", price)
                put("nbAdult", nbAdulte.text.toString().toInt())
                put("nbChildren", nbChildren.text.toString().toInt())
                put("travelClass", classSpinner.selectedItem.toString())
            }

            val request = object : JsonObjectRequest(Method.POST, url, jsonObject,
                { response ->
                    try {
                        Toast.makeText(this, "Booking successful!", Toast.LENGTH_SHORT).show()
                        Log.d("BookingResponse", response.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    error.printStackTrace()
                }) {
            }


            requestQueue?.add(request)


        }
    }
}