package com.example.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class transportBookingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<TransportBooking>()
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transport_booking, container, false)
        recyclerView = view.findViewById(R.id.recyclerviewTransportBooking)
        getRequest()
        return view
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.apit.getTransportBookings()
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val transportBookings = response.body()
                        if (transportBookings != null) {
                            for (transportBooking in transportBookings) {
                                val transportItem = TransportBooking(
                                    transportBooking._id,
                                    transportBooking.name ?: "",
                                    transportBooking.pays ?: "",
                                    transportBooking.title ?: "",
                                    transportBooking.location ?: "",
                                    transportBooking.price ?: "",
                                    transportBooking.description ?: "",
                                    transportBooking.nbPersonne ?: 0,
                                    transportBooking.date ?: "",
                                    transportBooking.luggage ?: 0
                                )
                                mList.add(transportItem)
                            }

                            withContext(Dispatchers.Main) {
                                myAdapter = TransportBookingAdapter(mList)
                                myAdapter.notifyDataSetChanged()

                                manager = LinearLayoutManager(requireContext())
                                recyclerView.layoutManager = manager
                                recyclerView.adapter = myAdapter
                            }
                        }}}
            }catch (e: HttpException){
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "HTTP error ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("transportError", e.toString())
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "App error ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("transportError", e.toString())
                }
            }
        }
    }
}

