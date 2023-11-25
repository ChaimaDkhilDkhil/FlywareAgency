package com.example.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
class bookingFlightAdapter(var mList: ArrayList<Booking>, var requestQueue: RequestQueue?) : RecyclerView.Adapter<bookingFlightAdapter.bookingViewHolder>() {

    inner class bookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val duration: TextView = itemView.findViewById(R.id.hotelLocationList)
        val date: TextView = itemView.findViewById(R.id.hotelCountryList)
        val returndate: TextView = itemView.findViewById(R.id.returndate)
        val destination: TextView = itemView.findViewById(R.id.hotelNameList)
        val departure: TextView = itemView.findViewById(R.id.departure)
        val price: TextView = itemView.findViewById(R.id.hotePriceList)
        val update: FloatingActionButton = itemView.findViewById(R.id.update)
        val delete: FloatingActionButton = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookingview, parent, false)
        return bookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: bookingViewHolder, position: Int) {
        val currentItem = mList[position]

        holder.duration.text = "${currentItem.duration}"
        holder.date.text = "${currentItem.date}"
        holder.returndate.text = "${currentItem.returnDate}"
        holder.destination.text = "${currentItem.destination}"
        holder.departure.text = "${currentItem.departure}"
        holder.price.text = "${currentItem.price} $"

        holder.update.setOnClickListener {
            val intent = Intent(holder.itemView.context, bookingUpdate::class.java)
            intent.putExtra("id", currentItem.id)
            intent.putExtra("duration", currentItem.duration)
            intent.putExtra("date", currentItem.date)
            intent.putExtra("returnDate", currentItem.returnDate)
            intent.putExtra("destination", currentItem.destination)
            intent.putExtra("departure", currentItem.departure)
            intent.putExtra("price", currentItem.price)
            intent.putExtra("nbAdult", currentItem.nbAdult)
            intent.putExtra("nbChildren", currentItem.nbChildren)
            intent.putExtra("travelClass", currentItem.clase)

            holder.itemView.context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            deleteItem(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Method to delete an item from the list and JSON file
    private fun deleteItem(position: Int) {
        val currentItem = mList[position]

        // Assuming your server URL for deleting is like: .../bookings/{bookingId}
        val deleteUrl = "http://192.168.56.1:3000/bookings/${currentItem.id}"
        val deleteRequest = StringRequest(Request.Method.DELETE, deleteUrl,
            { response ->
                // Delete the item from the list
                mList.removeAt(position)
                notifyDataSetChanged()
            },
            { error ->
                // Handle the error
            })

        // Add the request to the request queue
        requestQueue?.add(deleteRequest)
    }
}
