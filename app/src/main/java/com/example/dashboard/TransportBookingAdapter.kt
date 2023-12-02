package com.example.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
class TransportBookingAdapter (var mList: ArrayList<TransportBooking>) : RecyclerView.Adapter<TransportBookingAdapter.bookingViewHolder>() {

    inner class bookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.transportLocationList)
        val country: TextView = itemView.findViewById(R.id.transportCountryList)
        val name: TextView = itemView.findViewById(R.id.transportNameList)
        val price: TextView = itemView.findViewById(R.id.transportPriceList)
        val update: FloatingActionButton = itemView.findViewById(R.id.updateTransport)
        val delete: FloatingActionButton = itemView.findViewById(R.id.deleteTransport)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transport_booking_list_layout, parent, false)
        return bookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: bookingViewHolder, position: Int) {
        val currentItem = mList[position]

        holder.location.text = "${currentItem.location}"
        holder.country.text = "${currentItem.pays}"
        holder.name.text = "${currentItem.name}"
        holder.price.text = "${currentItem.price}"


        holder.update.setOnClickListener {
            val intent = Intent(holder.itemView.context, BookingTransportUpdate::class.java)
            intent.putExtra("id", currentItem._id)
            intent.putExtra("name", currentItem.name)
            intent.putExtra("pays", currentItem.pays)
            intent.putExtra("location", currentItem.location)
            intent.putExtra("price", currentItem.price)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("nbpersonne", currentItem.nbPersonne)
            intent.putExtra("date", currentItem.date)
            intent.putExtra("luggage", currentItem.luggage)

            holder.itemView.context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val response = try {
                    RetrofitInstance.apit.deletePost(currentItem._id.toString())
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
                        Toast.makeText(holder.itemView.context, "Erreur réseau: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(holder.itemView.context, "Suppression réussie", Toast.LENGTH_SHORT).show()
                        mList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, mList.size)
                    }
                }
            }        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }




}