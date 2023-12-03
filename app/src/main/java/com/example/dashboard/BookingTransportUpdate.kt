package com.example.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
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

class BookingTransportUpdate : AppCompatActivity() {

    lateinit var id: String
    lateinit var name: String
    lateinit var pays: String
    lateinit var location: String
    lateinit var price: String
    lateinit var description: String
    lateinit var title: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_transport_update)

        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("name").toString()
        pays = intent.getStringExtra("pays").toString()
        location = intent.getStringExtra("location").toString()
        price = intent.getStringExtra("price").toString()
        description = intent.getStringExtra("description").toString()
        title = intent.getStringExtra("title").toString()

        val nbPersonne = intent.getIntExtra("nbpersonne", 0)
        val datevar = intent.getStringExtra("date")
        val luggage = intent.getIntExtra("luggage", 0)

        findViewById<TextView>(R.id.transportLocationUp).setText(location)
        findViewById<TextView>(R.id.transportPriceUp).setText(price)
        findViewById<TextView>(R.id.nbpersonneUp).setText(nbPersonne.toString())
        findViewById<TextView>(R.id.dateUp).setText(datevar)
        findViewById<TextView>(R.id.titleUp).setText(name)
        findViewById<TextView>(R.id.transportluggageUp).setText(luggage.toString())

        val date = findViewById<EditText>(R.id.dateUp)
        date.setText(datevar.toString())
        date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                    runOnUiThread {
                        date.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                    }
                },
                year,
                month,
                day
            )
            datePickerDialog.show()

        }
        val button = findViewById<Button>(R.id.transportbtnUp)
        button.setOnClickListener {
            putRequest()

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun putRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val nbpersonne = findViewById<EditText>(R.id.nbpersonneUp).text.toString().toInt()
                val date = findViewById<EditText>(R.id.dateUp).text.toString()
                val luggage = findViewById<EditText>(R.id.transportluggageUp).text.toString().toInt()

                val updatedTransportBooking = TransportBooking(
                    id,
                    name,
                    pays,
                    title,
                    location,
                    price,
                    description,
                    nbpersonne,
                    date,
                    luggage
                )

                val response = try {
                    RetrofitInstance.apit.putPost(id, updatedTransportBooking)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Erreur HTTP ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    return@launch
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Erreur de l'application ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    return@launch
                }

                withContext(Dispatchers.Main) {
                    runOnUiThread {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(
                                this@BookingTransportUpdate,
                                "Mise à jour réussie",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@BookingTransportUpdate,
                                "Échec de la mise à jour",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: NumberFormatException) {
                withContext(Dispatchers.Main) {
                    runOnUiThread {
                        Toast.makeText(
                            this@BookingTransportUpdate,
                            "Veuillez entrer des valeurs valides pour nbPersonne et Luggage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

}