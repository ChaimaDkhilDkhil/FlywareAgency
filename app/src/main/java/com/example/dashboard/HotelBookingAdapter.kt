package com.example.dashboard

import android.content.Intent
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
class HotelBookingAdapter(var mList: ArrayList<HotelBooking>) :
    RecyclerView.Adapter<HotelBookingAdapter.bookingViewHolder>() {

    inner class bookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.hotelLocationList)
        val country: TextView = itemView.findViewById(R.id.hotelCountryList)
        val name: TextView = itemView.findViewById(R.id.hotelNameList)
        val price: TextView = itemView.findViewById(R.id.hotelPriceList)
        val hotelstatus: TextView = itemView.findViewById(R.id.hotelstatus)

        val update: FloatingActionButton = itemView.findViewById(R.id.updateHotel)
        val delete: FloatingActionButton = itemView.findViewById(R.id.deleteHotel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hotel_booking_list_layout, parent, false)
        return bookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: bookingViewHolder, position: Int) {
        val currentItem = mList[position]

        holder.location.text = "${currentItem.location}"
        holder.country.text = "${currentItem.pays}"
        holder.name.text = "${currentItem.name}"
        holder.price.text = "${currentItem.price}"

        if (currentItem.status == "en attente") {
            holder.hotelstatus.visibility = View.GONE
            holder.update.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE

            holder.update.setOnClickListener {
                val intent = Intent(holder.itemView.context, BookingHotelUpdate::class.java)
                intent.putExtra("id", currentItem._id)
                intent.putExtra("name", currentItem.name)
                intent.putExtra("pays", currentItem.pays)
                intent.putExtra("location", currentItem.location)
                intent.putExtra("price", currentItem.price)
                intent.putExtra("description", currentItem.description)
                intent.putExtra("nbRoom", currentItem.nbRoom)
                intent.putExtra("date", currentItem.date)
                intent.putExtra("duration", currentItem.duration)

                holder.itemView.context.startActivity(intent)
            }

            holder.delete.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    val response = try {
                        RetrofitInstance.api.deletePost(currentItem._id.toString())
                    } catch (e: HttpException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Erreur HTTP ${e.code()}: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        return@launch
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Erreur réseau: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        return@launch
                    }

                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Suppression réussie",
                                Toast.LENGTH_SHORT
                            ).show()
                            mList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, mList.size)
                        }
                    }
                }
            }
        } else {
            holder.hotelstatus.visibility = View.VISIBLE
            holder.update.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.hotelstatus.text = "${currentItem.status}"
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
