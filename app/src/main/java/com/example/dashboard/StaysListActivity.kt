package com.example.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class StaysListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<Hotels>
    private var mList = ArrayList<Hotels>()
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: StaysAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<Hotels>
    private lateinit var pays :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stays_list)
        pays= intent.getStringExtra("countryName")!!
        recyclerView = findViewById(R.id.hotelRecyclerView)
        searchView = findViewById(R.id.hotelSearch)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf()
        searchList = arrayListOf()
        getRequest()
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    dataList.forEach{
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
        myAdapter = StaysAdapter(searchList)
        recyclerView.adapter = myAdapter
        myAdapter.onItemClick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("countryName", it.pays)
            this.startActivity(intent)
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun getRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getHotels(pays)
            }catch (e: HttpException){
                Toast.makeText(applicationContext,"http error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }catch (e: IOException){
                Toast.makeText(applicationContext,"app error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null){
                withContext(Dispatchers.Main){

                    val hotels = response.body()
                    if (hotels != null) {
                        for(hotel in hotels) {
                            val drawableName = hotel.hotel.name.toLowerCase(Locale.ROOT)
                            val drawableId = resources.getIdentifier(drawableName, "drawable", packageName)
                            val hotelItem = Hotels(
                                hotel.pays,
                                hotel.hotel,
                                drawableId
                            )
                            mList.add(hotelItem)
                            myAdapter.notifyDataSetChanged()

                        }
                    }

                    manager = LinearLayoutManager(this@StaysListActivity)
                    myAdapter = StaysAdapter(mList)

                    recyclerView.layoutManager = manager
                    recyclerView.adapter = myAdapter
                }
            }}
    }
}