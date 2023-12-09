package com.example.dashboard

import android.content.Intent
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
import org.json.JSONObject

class bookingUpdate : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_update)
        val classSpinner: Spinner = findViewById(R.id.classSpinnerUp)

        val classes = arrayOf("Business", "Economy")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        classSpinner.adapter = adapter
        val id = intent.getStringExtra("id")
        val destination = intent.getStringExtra("destination")
        val departureDate = intent.getStringExtra("date")
        val duration = intent.getStringExtra("duration")
        val returnDate = intent.getStringExtra("returnDate")
        val departure = intent.getStringExtra("departure")
        val price = intent.getDoubleExtra("price", 0.0)
        val nbAdult = intent.getIntExtra("nbAdult", 0)
        val nbChildren = intent.getIntExtra("nbChildren", 0)
        val travelClass = intent.getStringExtra("travelClass")
        requestQueue = Volley.newRequestQueue(this)

        findViewById<TextView>(R.id.destinationUp).setText(destination)
        findViewById<TextView>(R.id.dateUp).setText  (departureDate)
        findViewById<TextView>(R.id.durationUp).setText (duration)
        findViewById<TextView>(R.id.returnDateUp).setText  (returnDate)
        findViewById<TextView>(R.id.departureUp).setText (departure)
        findViewById<TextView>(R.id.priceUp).setText (price.toString())

        findViewById<EditText>(R.id.nbAdultUp).setText(nbAdult.toString())
        findViewById<EditText>(R.id.nbChildrenUp).setText(nbChildren.toString())
        val spinner=findViewById<Spinner>(R.id.classSpinnerUp)

         val spinnerAdapter = spinner.adapter
        if (spinnerAdapter is ArrayAdapter<*>) {
            val position = (spinnerAdapter as ArrayAdapter<String>).getPosition(travelClass)
            if (position != -1) {
                spinner.setSelection(position)
            }
        }
        val button=findViewById<Button>(R.id.updatebtn)
        button.setOnClickListener {
            val nbAdult = findViewById<EditText>(R.id.nbAdultUp).text.toString().toInt()
            val nbChildren = findViewById<EditText>(R.id.nbChildrenUp).text.toString().toInt()
            val travelClass = findViewById<Spinner>(R.id.classSpinnerUp).selectedItem.toString()

            val updatedData = JSONObject().apply {
                put("nbAdult", nbAdult)
                put("nbChildren", nbChildren)
                put("travelClass", travelClass)
            }
            val url = "http://192.168.56.1:3000/bookings/${id}"

            val request = JsonObjectRequest(
                Request.Method.PUT, url, updatedData,
                { response ->
                    Toast.makeText(this, "flight booking updated successfully", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Toast.makeText(this, "Error updating JSON file", Toast.LENGTH_SHORT).show()
                })
            requestQueue.add(request)
        }
    }



}