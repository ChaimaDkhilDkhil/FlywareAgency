package com.example.dashboard

import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale


class FlightActivity : MainActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<flight_items_view_model>()
    private lateinit var adapter: flight_adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_view) // Replace with your layout resource ID


        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        addDataToList()
        adapter = flight_adapter(mList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<flight_items_view_model>()
            for (i in mList) {
                if (i.title.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addDataToList(){
        mList.add(flight_items_view_model("Flight to France", R.drawable.img_8))
        mList.add(flight_items_view_model("Flight to Canada", R.drawable.img_1))
        mList.add(flight_items_view_model("Flight to Sweden", R.drawable.img_2))
        mList.add(flight_items_view_model("Flight to Belgium", R.drawable.img_3))
        mList.add(flight_items_view_model("Flight to America", R.drawable.img_4))
        mList.add(flight_items_view_model("Flight to Suisse", R.drawable.img_5))
        mList.add(flight_items_view_model("Flight to Italie", R.drawable.img_6))
        mList.add(flight_items_view_model("Flight to Mexico", R.drawable.img_7))
    }


}