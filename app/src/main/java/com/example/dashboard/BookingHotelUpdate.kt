package com.example.dashboard

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar

class BookingHotelUpdate : AppCompatActivity() {
    lateinit var id :String
    lateinit var name :String
    lateinit var pays :String
    lateinit var location :String
    lateinit var price :String
    lateinit var description :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_hotel_update)

         id = intent.getStringExtra("id").toString()
         name = intent.getStringExtra("name").toString()
         pays = intent.getStringExtra("pays").toString()
         location = intent.getStringExtra("location").toString()
         price = intent.getStringExtra("price").toString()
        description = intent.getStringExtra("description").toString()

        val nbRoom = intent.getIntExtra("nbRoom", 0)
        val datevar = intent.getStringExtra("date")
        val duration = intent.getIntExtra("duration", 0)

        findViewById<TextView>(R.id.hotelNameUp).setText(name)
        findViewById<TextView>(R.id.hotelLocationUp).setText(location)
        findViewById<TextView>(R.id.hotelPriceUp).setText (price)
        findViewById<TextView>(R.id.nbRoomUp).setText  (nbRoom.toString())
        findViewById<TextView>(R.id.dateUp).setText (datevar)
        findViewById<TextView>(R.id.hotelDurationUp).setText (duration.toString())

        val date= findViewById<EditText>(R.id.dateUp)
        date.setText(datevar.toString())
        date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                runOnUiThread {
                    date.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                }
            }, year, month, day)
            datePickerDialog.show()

        }
        val button=findViewById<Button>(R.id.hotelbtnUp)
        button.setOnClickListener {
      putRequest()

        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun putRequest(){
        GlobalScope.launch(Dispatchers.IO) {
            val nbRoom = findViewById<EditText>(R.id.nbRoomUp).text.toString().toInt()
            val date = findViewById<EditText>(R.id.dateUp).text.toString()
            val duration = findViewById<EditText>(R.id.hotelDurationUp).text.toString().toInt()

            val updatedHotelBooking = HotelBooking(
                id,
                pays,
                name,
                location,
                price,
                description,
                nbRoom,
                date,
                duration
            )
            val response = try {

                RetrofitInstance.api.putPost(id,updatedHotelBooking)

            }catch (e: HttpException){
                Toast.makeText(applicationContext,"http error ${e.message}",Toast.LENGTH_LONG).show()
                return@launch
            }catch (e: IOException){
                Toast.makeText(applicationContext,"app error ${e.message}",Toast.LENGTH_LONG).show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null){
                withContext(Dispatchers.Main){
                    runOnUiThread {

                        Toast.makeText(this@BookingHotelUpdate, "Mise à jour réussie", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}