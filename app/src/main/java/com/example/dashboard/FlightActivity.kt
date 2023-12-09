package com.example.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.Locale


class FlightActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<flight_items_view_model>()
    private lateinit var adapter: flight_adapter
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    private var requestQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_view)


        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        requestQueue = Volley.newRequestQueue(this)

        jsonParse()


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
        adapter = flight_adapter(mList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun jsonParse() {
        val url = "http://192.168.56.1:3000/flights"
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            try {
                Log.e("resLen",response.length().toString())
                for (i in 0 until response.length()) {
                    val user = response.getJSONObject(i)
                    val id = user.getString("_id")
                    val duration = user.getString("duration")
                    val date = user.getString("date")
                    val returnDate = user.getString("returnDate")
                    val destination = user.getString("destination")
                    val departure = user.getString("departure")
                    val price = user.getDouble("price")

                    val drawableName = destination.toLowerCase(Locale.ROOT)
                    val drawableId = resources.getIdentifier(drawableName, "drawable", packageName)

                    val flightItem = flight_items_view_model(
                        id,
                        destination,
                        date,
                        returnDate,
                        departure,
                        price,
                        duration,
                        drawableId
                    )
                    mList.add(flightItem)
                }

                manager = LinearLayoutManager(this)
                myAdapter = flight_adapter(mList)

                recyclerView.layoutManager = manager
                recyclerView.adapter = myAdapter


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error -> error.printStackTrace()
          })
        requestQueue?.add(request)
    }


    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<flight_items_view_model>()
            for (i in mList) {
                if (i.destination.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {

                adapter.setFilteredList(filteredList)
                Log.d("FilteredList", filteredList.size.toString())


            }
        }
    }
}