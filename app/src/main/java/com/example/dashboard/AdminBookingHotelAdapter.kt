package com.example.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AdminBookingHotelAdapter (var mList: ArrayList<HotelBooking>) : RecyclerView.Adapter<AdminBookingHotelAdapter.bookingViewHolder>() {

    inner class bookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.AdminHotelLocationList)
        val country: TextView = itemView.findViewById(R.id.adminHotelCountryList)
        val name: TextView = itemView.findViewById(R.id.AdminHotelNameList)
        val price: TextView = itemView.findViewById(R.id.AdminHotelPriceList)
        val hotelstatus: TextView = itemView.findViewById(R.id.Adminhotelstatus)

        val accept: FloatingActionButton = itemView.findViewById(R.id.acceptHotel)
        val refuse: FloatingActionButton = itemView.findViewById(R.id.refuseHotel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_hotel_booking_view, parent, false)
        return bookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: bookingViewHolder, position: Int) {
        var currentItem = mList[position]

        holder.location.text = "${currentItem.location}"
        holder.country.text = "${currentItem.pays}"
        holder.name.text = "${currentItem.name}"
        holder.price.text = "${currentItem.price}"

        if (currentItem.status == "en attente") {
            holder.hotelstatus.visibility = View.GONE
            holder.accept.visibility = View.VISIBLE
            holder.refuse.visibility = View.VISIBLE

            holder.accept.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {

                val response = try {
                    currentItem.status="accepted"
                    RetrofitInstance.api.putStatusHotel(currentItem._id.toString(),currentItem)

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
                        RetrofitInstance.api.putStatusHotel(currentItem._id.toString(),currentItem)

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
            holder.hotelstatus.visibility = View.VISIBLE
            holder.accept.visibility = View.GONE
            holder.refuse.visibility = View.GONE
            holder.hotelstatus.text = "${currentItem.status}"
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
