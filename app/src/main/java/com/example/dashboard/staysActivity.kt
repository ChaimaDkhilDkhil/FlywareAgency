package com.example.dashboard
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale


class staysActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var imageList:Array<Int>
    private lateinit var titleList:Array<String>
    private lateinit var descList: Array<String>
    private lateinit var detailImageList: Array<Int>
    private lateinit var myAdapter: AdapterClass
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<DataClass>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stays_main)
        imageList = arrayOf(
            R.drawable.france,
            R.drawable.canada,
            R.drawable.sweden,
            R.drawable.belgium,
            R.drawable.america,
            R.drawable.suisse,
            R.drawable.italie,
            R.drawable.mexico,
            R.drawable.tunisia,
            R.drawable.germany)
        titleList = arrayOf(
            "Stays in France",
            "Stays in Canada",
            "Stays in Sweden",
            "Stays in Belgium",
            "Stays in America",
            "Stays in Suisse",
            "Stays in Italie",
            "Stays in Mexico",
            "Stays in Tunisia",
            "Stays in Germany")
        descList = arrayOf(
            getString(R.string.France),
            getString(R.string.Canada),
            getString(R.string.Sweden),
            getString(R.string.Belgium),
            getString(R.string.America),
            getString(R.string.Suisse),
            getString(R.string.Italie),
            getString(R.string.Mexico),
            getString(R.string.Tunisia),
            getString(R.string.Germany))
        detailImageList = arrayOf(
            R.drawable.france_hotels,
            R.drawable.canada_hotels,
            R.drawable.sweden_hotels,
            R.drawable.belgium_hotels,
            R.drawable.america_hotels,
            R.drawable.suisse_hotels,
            R.drawable.italie_hotels,
            R.drawable.mexico_hotels,
            R.drawable.tunisia_hotels,
            R.drawable.germany_hotels)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.search)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        dataList = arrayListOf()
        searchList = arrayListOf()
        getData()
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
                        if (it.dataTitle.lowercase(Locale.getDefault()).contains(searchText)) {
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
        myAdapter = AdapterClass(searchList)
        recyclerView.adapter = myAdapter
        myAdapter.onItemClick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("android", it)
            startActivity(intent)
        }
    }
    private fun getData(){
        for (i in imageList.indices){
            val dataClass = DataClass(imageList[i], titleList[i], descList[i], detailImageList[i])
            dataList.add(dataClass)
        }
        searchList.addAll(dataList)
        recyclerView.adapter = AdapterClass(searchList)
    }
}