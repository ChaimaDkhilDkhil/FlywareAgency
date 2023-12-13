package com.example.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request.Method
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class AdminBookingFlightAdapter(var mList: ArrayList<Booking>, var requestQueue: RequestQueue?) : RecyclerView.Adapter<AdminBookingFlightAdapter.AdminBookingViewHolder>() {

    inner class AdminBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val duration: TextView = itemView.findViewById(R.id.adminFlightDurationList)
        val date: TextView = itemView.findViewById(R.id.adminFlightDateList)
        val returndate: TextView = itemView.findViewById(R.id.adminFlightreturndate)
        val destination: TextView = itemView.findViewById(R.id.adminFlightDestinationList)
        val departure: TextView = itemView.findViewById(R.id.adminFlightdeparture)
        val price: TextView = itemView.findViewById(R.id.adminFlightPriceList)
        val status: TextView = itemView.findViewById(R.id.adminFlightStatus)
        val accept: FloatingActionButton = itemView.findViewById(R.id.acceptFlight)
        val refuse: FloatingActionButton = itemView.findViewById(R.id.refuseFlight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_flight_booking_view, parent, false)
        return AdminBookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminBookingViewHolder, position: Int) {
        val currentItem = mList[position]

        holder.duration.text = "${currentItem.flight.duration}"
        holder.date.text = "${currentItem.flight.date}"
        holder.returndate.text = "${currentItem.flight.returnDate}"
        holder.destination.text = "${currentItem.flight.destination}"
        holder.departure.text = "${currentItem.flight.departure}"
        holder.price.text = "${currentItem.flight.price} $"

        if (currentItem.status == "en attente") {
            holder.status.visibility = View.GONE
            holder.accept.visibility = View.VISIBLE
            holder.refuse.visibility = View.VISIBLE

            holder.accept.setOnClickListener {
                val updateStatusUrl = "http://192.168.56.1:3000/status/${currentItem._id}"
                val updatedData = JSONObject().apply {
                    put("status", "accepted")}
                val updateStatusRequest = JsonObjectRequest(Method.PUT, updateStatusUrl,updatedData,
                    { userResponse ->
                        currentItem.status = "accepted"
                        notifyDataSetChanged()
                    },
                    { error ->
                    })
                requestQueue?.add(updateStatusRequest)
            }

            holder.refuse.setOnClickListener {
                val updateStatusUrl = "http://192.168.56.1:3000/status/${currentItem._id}"
                val updatedData = JSONObject().apply {
                    put("status", "refused")}
                val updateStatusRequest = JsonObjectRequest(Method.PUT, updateStatusUrl,updatedData,
                    { response ->
                        currentItem.status = "refused"
                        notifyDataSetChanged()
                    },
                    { error ->
                    })
                requestQueue?.add(updateStatusRequest)
            }
        } else {
            holder.status.visibility = View.VISIBLE
            holder.accept.visibility = View.GONE
            holder.refuse.visibility = View.GONE
            holder.status.text = "${currentItem.status}"
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
