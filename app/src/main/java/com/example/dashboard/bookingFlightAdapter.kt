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
        val status: TextView = itemView.findViewById(R.id.status)
        val update: FloatingActionButton = itemView.findViewById(R.id.update)
        val delete: FloatingActionButton = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookingview, parent, false)
        return bookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: bookingViewHolder, position: Int) {
        val currentItem = mList[position]

        holder.duration.text = "${currentItem.flight.duration}"
        holder.date.text = "${currentItem.flight.date}"
        holder.returndate.text = "${currentItem.flight.returnDate}"
        holder.destination.text = "${currentItem.flight.destination}"
        holder.departure.text = "${currentItem.flight.departure}"
        holder.price.text = "${currentItem.flight.price} $"

        if (currentItem.status == "en attente") {
            holder.status.visibility = View.GONE
            holder.update.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE
        holder.update.setOnClickListener {
            val intent = Intent(holder.itemView.context, bookingUpdate::class.java)
            intent.putExtra("id", currentItem._id)
            intent.putExtra("duration", currentItem.flight.duration)
            intent.putExtra("date", currentItem.flight.date)
            intent.putExtra("returnDate", currentItem.flight.returnDate)
            intent.putExtra("destination", currentItem.flight.destination)
            intent.putExtra("departure", currentItem.flight.departure)
            intent.putExtra("price", currentItem.flight.price)
            intent.putExtra("nbAdult", currentItem.nbAdult)
            intent.putExtra("nbChildren", currentItem.nbChildren)
            intent.putExtra("travelClass", currentItem.travelClass)
            holder.itemView.context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            deleteItem(position)}
        } else {
            holder.status.visibility = View.VISIBLE
            holder.update.visibility = View.GONE
                holder.delete.visibility = View.GONE
                holder.status.text = "${currentItem.status}"
            }
        }
    override fun getItemCount(): Int {
        return mList.size
    }

    private fun deleteItem(position: Int) {
        val currentItem = mList[position]

        val deleteUrl = "http://192.168.56.1:3000/bookings/${currentItem._id}"
        val deleteRequest = StringRequest(Request.Method.DELETE, deleteUrl,
            { response ->
                mList.removeAt(position)
                notifyDataSetChanged()
            },
            { error ->
            })

        requestQueue?.add(deleteRequest)
    }
}
