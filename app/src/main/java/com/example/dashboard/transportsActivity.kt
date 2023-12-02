package com.example.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class transportsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<Transports>
    private var mList = ArrayList<Transports>()
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: AdapterClassTransport
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<Transports>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transports)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.search)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf()
        searchList = arrayListOf()
        getRequest()
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dataList.forEach {
                        if (it.pays.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(dataList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })

        myAdapter = AdapterClassTransport(searchList)
        recyclerView.adapter = myAdapter
        myAdapter.onItemClick = {
            val intent = Intent(this, TransportsListActivity::class.java)
            intent.putExtra("country", it.pays)
            this.startActivity(intent)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.apit.getCountries()
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val transports = response.body()
                    if (transports != null) {
                        for (transport in transports) {
                            val drawableName = transport.toLowerCase(Locale.ROOT)
                            val drawableId = resources.getIdentifier(drawableName, "drawable", packageName)
                            val transportItem = Transports(
                                transport, // pays
                                Transport("", "", "", ""), // transport (you might need to adjust this based on your Transport class)
                                drawableId // img
                            )
                            mList.add(transportItem)
                            myAdapter.notifyDataSetChanged()
                        }
                    }

                    manager = LinearLayoutManager(this@transportsActivity)
                    myAdapter = AdapterClassTransport(mList)

                    recyclerView.layoutManager = manager
                    recyclerView.adapter = myAdapter
                }
            }
        }
    }
}