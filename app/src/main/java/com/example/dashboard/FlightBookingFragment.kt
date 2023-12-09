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
import com.android.volley.toolbox.JsonObjectRequest
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
                    val book = response.getJSONObject(i)
                    val id = book.getString("_id")

                    val userId = book.getString("user")
                    val userUrl = "http://192.168.56.1:3000/user/$userId"
                    val userRequest = JsonObjectRequest(Request.Method.GET, userUrl, null,
                        { userResponse ->
                            val user = User(
                                userResponse.getString("_id"),
                                userResponse.getString("username"),
                                userResponse.getString("password"),
                                userResponse.getString("token")
                            )
                            val flightId = book.getString("flight")
                            val flightUrl = "http://192.168.56.1:3000/flights/$flightId"
                            val flightRequest = JsonObjectRequest(Request.Method.GET, flightUrl, null,
                                { flightResponse ->
                                    val flight = flight_items_view_model(
                                        flightResponse.getString("_id"),
                                        flightResponse.getString("destination"),
                                        flightResponse.getString("date"),
                                        flightResponse.getString("returnDate"),
                                        flightResponse.getString("departure"),
                                        flightResponse.getDouble("price"),
                                        flightResponse.getString("duration"),
                                        0
                                    )

                                    val nbAdult = book.getInt("nbAdult")
                                    val nbChildren = book.getInt("nbChildren")
                                    val travelClass = book.getString("travelClass")
                                    val status=book.getString("status")
                                    val booking = Booking(
                                        id,
                                        user,
                                        flight,
                                        nbChildren,
                                        nbAdult,
                                        travelClass,
                                        status
                                    )
                                    mList.add(booking)

                                    manager = LinearLayoutManager(requireContext())
                                    myAdapter = bookingFlightAdapter(mList, requestQueue)
                                    recyclerView.layoutManager = manager
                                    recyclerView.adapter = myAdapter
                                },
                                { error -> error.printStackTrace() }
                            )

                            requestQueue?.add(flightRequest)
                        },
                        { error -> error.printStackTrace() }
                    )

                    requestQueue?.add(userRequest)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace() })

        requestQueue?.add(request)

    }}