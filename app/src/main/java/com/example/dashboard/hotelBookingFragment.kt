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

class hotelBookingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<HotelBooking>()
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_booking, container, false)
        recyclerView = view.findViewById(R.id.recyclerviewHotelBooking)
        getRequest()
        return view
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun getRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getHotelBookings()
            }catch (e: HttpException){
                Toast.makeText(requireContext(),"http error ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("hotelError",e.toString())
                return@launch
            }catch (e: IOException){
                Toast.makeText(requireContext(),"app error ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("hotelError",e.toString())
                return@launch
            }

            if (response.isSuccessful && response.body() != null){
                withContext(Dispatchers.Main) {
                    val hotelBookings = response.body()
                    if (hotelBookings != null) {
                        for(hotelBooking in hotelBookings) {
                            val hotelItem = HotelBooking(
                                hotelBooking._id,
                                hotelBooking.pays,
                                hotelBooking.name,
                                hotelBooking.location,
                                hotelBooking.price,
                                hotelBooking.description,
                                hotelBooking.nbRoom,
                                hotelBooking.date,
                                hotelBooking.duration ,
                                hotelBooking.status
                            )
                            mList.add(hotelItem)
                        }
                        myAdapter = HotelBookingAdapter(mList)
                        myAdapter.notifyDataSetChanged()

                        manager = LinearLayoutManager(requireContext())
                        recyclerView.layoutManager = manager
                        recyclerView.adapter = myAdapter
                    }
                }

            }}
    }
    }