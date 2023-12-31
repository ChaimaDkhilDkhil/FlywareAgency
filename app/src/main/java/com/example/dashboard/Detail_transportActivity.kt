package com.example.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.app.DatePickerDialog
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

class Detail_transportActivity : AppCompatActivity() {

    private lateinit var nbpersonne: EditText
    private lateinit var date: EditText
    private lateinit var luggage: EditText
    private lateinit var btnbook: Button
    private lateinit var detailTitle: TextView
    private lateinit var detailDesc: TextView
    private lateinit var price: TextView
    private lateinit var location: TextView
    private lateinit var pays: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transport)

        nbpersonne = findViewById(R.id.nbpersonne)
        date = findViewById(R.id.transportdateList)
        luggage = findViewById(R.id.transportluggageList)
        btnbook = findViewById(R.id.transportaddbtn)

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
        pays = findViewById(R.id.transportPaysAdd)
        detailTitle = findViewById(R.id.transportNameAdd)
        detailDesc = findViewById(R.id.transportDescriptionAdd)
        price = findViewById(R.id.transportPriceAdd)
        location = findViewById(R.id.transportLocationAdd)
        val detailImage: ImageView = findViewById(R.id.transportImageAdd)
        detailTitle.text = intent.getStringExtra("name")
        detailDesc.text = intent.getStringExtra("description")
        price.text = intent.getStringExtra("price")
        location.text = intent.getStringExtra("location")
        pays.text= intent.getStringExtra("pays")
        detailImage.setImageResource(intent.getIntExtra("image", 0))

        btnbook.setOnClickListener {
            postRequest()
        }
    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun postRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.apit.createTransportBooking(
                    TransportBooking(
                        null,
                        detailTitle.text.toString(),
                        pays.text.toString(),
                        detailTitle.text.toString(),
                        location.text.toString(),
                        price.text.toString(),
                        detailDesc.text.toString(),
                        nbpersonne.text.toString().toIntOrNull() ?: 0, // Assuming nbPersonne is an Int
                        date.text.toString(), // Assuming date is a String
                        luggage.text.toString().toIntOrNull() ?: 0 // Assuming luggage is an Int
                    )
                )
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "HTTP error ${e.code()}: ${e.message}", Toast.LENGTH_LONG).show()
                }
                // Log the error message for debugging
                e.printStackTrace()
                return@launch
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "App error ${e.message}", Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
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