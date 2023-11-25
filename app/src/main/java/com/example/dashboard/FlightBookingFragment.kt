package com.example.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class FlightBookingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<Booking>()
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    private var requestQueue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flight_booking, container, false)
        recyclerView = view.findViewById(R.id.recyclerviewbooking)
        requestQueue = Volley.newRequestQueue(requireContext())
        jsonParse()
        return view
    }

    private fun jsonParse() {
        val url = "http://192.168.56.1:3000/bookings"
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            try {
                for (i in 0 until response.length()) {
                    val user = response.getJSONObject(i)
                    val id = user.getString("_id")
                    val duration = user.getString("duration")
                    val date = user.getString("date")
                    val returnDate = user.getString("returnDate")
                    val destination = user.getString("destination")
                    val departure = user.getString("departure")
                    val price = user.getDouble("price")
                    val nbAdult = user.getInt("nbAdult")
                    val nbChildren = user.getInt("nbChildren")
                    val travelClass = user.getString("travelClass")


                    val booking = Booking(
                        id,
                        destination,
                        date,
                        returnDate,
                        departure,
                        price,
                        duration,
                        nbChildren ,
                        nbAdult,
                        travelClass
                    )
                    mList.add(booking)
                }

                manager = LinearLayoutManager(requireContext())
                myAdapter = bookingFlightAdapter(mList,requestQueue)

                recyclerView.layoutManager = manager
                recyclerView.adapter = myAdapter

            } catch (e: JSONException) {
                e.printStackTrace()

            }
        }, { error -> error.printStackTrace()
        })
        requestQueue?.add(request)
    }
}