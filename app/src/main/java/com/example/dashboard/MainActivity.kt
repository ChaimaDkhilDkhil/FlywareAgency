package com.example.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

open class MainActivity : AppCompatActivity() {
    private var adapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerViewList = findViewById(R.id.view)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewList?.layoutManager = linearLayoutManager

        val news = ArrayList<ListDomain>()
        news.add(ListDomain("Browsing trips in Belgium", "pic1"))
        news.add(ListDomain("book a flight to tunisia   ", "tunisia3"))
        news.add(ListDomain("corea booking offer ", "corea3"))
        news.add(ListDomain("Stays in Roma", "roma2"))
        adapter = NewsAdapter(news)
        recyclerViewList?.adapter = adapter

        val flightButton=findViewById<Button>(R.id.flightButton)
        flightButton.setOnClickListener{
            val Intent = Intent(this,FlightActivity::class.java)
            startActivity(Intent)
        }

        val StaysButton=findViewById<Button>(R.id.StaysButton)
        StaysButton.setOnClickListener{
            val Intent = Intent(this,staysActivity::class.java)
            startActivity(Intent)
        }


    }
}
