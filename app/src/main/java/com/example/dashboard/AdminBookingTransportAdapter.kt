package com.example.dashboard

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AdminBookingTransportAdapter  (var mList: ArrayList<TransportBooking>) : RecyclerView.Adapter<AdminBookingTransportAdapter.bookingViewHolder>() {

    inner class bookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.adminTransportLocationList)
        val country: TextView = itemView.findViewById(R.id.adminTransportCountryList)
        val name: TextView = itemView.findViewById(R.id.adminTransportNameList)
        val price: TextView = itemView.findViewById(R.id.adminTransportPriceList)
        val transportstatus: TextView = itemView.findViewById(R.id.adminTransportstatus)
        val accept: FloatingActionButton = itemView.findViewById(R.id.acceptTransport)
        val refuse: FloatingActionButton = itemView.findViewById(R.id.refuseTransport)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_transport_booking_view, parent, false)
        return bookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: bookingViewHolder, position: Int) {
        try {
            val currentItem = mList[position]
            Log.e("Adapter", "CurrentItem: $currentItem")


        holder.location.text = "${currentItem.location}"
        holder.country.text = "${currentItem.pays}"
        holder.name.text = "${currentItem.name}"
        holder.price.text = "${currentItem.price}"

        if (currentItem.status == "en attente") {
            holder.transportstatus.visibility = View.GONE
            holder.accept.visibility = View.VISIBLE
            holder.refuse.visibility = View.VISIBLE
            holder.accept.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {

                    val response = try {
                        currentItem.status="accepted"
                        RetrofitInstance.apit.putStatusTransport(currentItem._id.toString(),currentItem)

                    }catch (e: HttpException){
                        Log.e("http error", e.message.toString())
                        return@launch
                    }catch (e: IOException){
                        Log.e("app error", e.message.toString())
                        return@launch
                    }
                    if (response.isSuccessful && response.body() != null){
                        withContext(Dispatchers.Main){
                            currentItem.status = "accepted"
                            notifyDataSetChanged()
                        }
                    }}
            }
            holder.refuse.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {

                    val response = try {
                        currentItem.status="refused"
                        RetrofitInstance.apit.putStatusTransport(currentItem._id.toString(),currentItem)

                    }catch (e: HttpException){
                        Log.e("http error", e.message.toString())
                        return@launch
                    }catch (e: IOException){
                        Log.e("app error", e.message.toString())
                        return@launch
                    }
                    if (response.isSuccessful && response.body() != null){
                        withContext(Dispatchers.Main){
                            currentItem.status = "refused"
                            notifyDataSetChanged()
                        }
                    }}}
        } else {
            holder.transportstatus.visibility = View.VISIBLE
            holder.accept.visibility = View.GONE
            holder.refuse.visibility = View.GONE
            holder.transportstatus.text = "${currentItem.status}"
        }}catch (e: Exception) {
            Log.e("Adapter", "Error in onBindViewHolder: ${e.message}")
            e.printStackTrace()
        }
    }
    override fun getItemCount(): Int {
        return mList.size
    }
}