package com.example.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
class HomeFragment : Fragment() {
    private var adapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewList: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewList = view.findViewById(R.id.view)

        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewList?.layoutManager = linearLayoutManager

        val news = ArrayList<ListDomain>()
        news.add(ListDomain("Browsing trips in Belgium", "pic1"))
        news.add(ListDomain("book a flight to tunisia   ", "tunisia3"))
        news.add(ListDomain("corea booking offer ", "corea3"))
        news.add(ListDomain("Stays in Roma", "roma2"))
        adapter = NewsAdapter(news)
        recyclerViewList?.adapter = adapter

        val flightButton = view.findViewById<Button>(R.id.flightButton)
        flightButton.setOnClickListener {
            val intent = Intent(requireContext(), FlightActivity::class.java)
            startActivity(intent)
        }

        val staysButton = view.findViewById<Button>(R.id.StaysButton)
        staysButton.setOnClickListener {
            val intent = Intent(requireContext(), staysActivity::class.java)
            startActivity(intent)
        }
        val transportsButton = view.findViewById<Button>(R.id.transportButton)
        transportsButton.setOnClickListener {
            val intent = Intent(requireContext(), transportsActivity::class.java)
            startActivity(intent)
        }

    }
}
