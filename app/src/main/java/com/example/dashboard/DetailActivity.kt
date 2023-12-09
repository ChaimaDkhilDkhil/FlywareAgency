package com.example.dashboard

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var nbRoom : EditText
    private lateinit var date : EditText
    private lateinit var duration : EditText
    private lateinit var btnbook : Button
    private lateinit var detailTitle: TextView
    private lateinit var detailDesc: TextView
    private lateinit var price: TextView
    private lateinit var location: TextView
    private lateinit var pays: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        nbRoom=findViewById(R.id.nbRoom)
        date=findViewById(R.id.hotelCountryList)
        duration=findViewById(R.id.hotelLocationList)
        btnbook=findViewById(R.id.hoteladdbtn)

        date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                date.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day)
            datePickerDialog.show()
        }
        detailTitle = findViewById(R.id.hotelNameAdd)
        detailDesc = findViewById(R.id.hotelDescriptionAdd)
        price = findViewById(R.id.hotelPriceAdd)
        location = findViewById(R.id.hotelLocationAdd)
        val detailImage: ImageView = findViewById(R.id.hotelImageAdd)
        detailTitle.text =intent.getStringExtra("name")
        detailDesc.text = intent.getStringExtra("description")
        price.text =intent.getStringExtra("price")
        location.text =  intent.getStringExtra("location")
        pays = intent.getStringExtra("pays").toString()

        detailImage.setImageResource(intent.getIntExtra("image",0))
        btnbook.setOnClickListener {
            postRequest()
        }
    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun postRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.createBooking(
                    HotelBooking(null,
                        pays,
                        detailTitle.text.toString(),
                        location.text.toString(),
                        price.text.toString(),
                        detailDesc.text.toString(),
                        nbRoom.text.toString().toIntOrNull() ?: 0,
                        date.text.toString(),
                        duration.text.toString().toIntOrNull() ?: 0
                    )
                )
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "HTTP error ${e.code()}: ${e.message}", Toast.LENGTH_LONG).show()
                }
                return@launch
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "App error ${e.message}", Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    Snackbar.make(
                        btnbook,
                        "Booking created successfully",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}