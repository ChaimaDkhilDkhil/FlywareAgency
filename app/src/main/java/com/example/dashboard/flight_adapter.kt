package com.example.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class flight_adapter(var mList: ArrayList<flight_items_view_model>) : RecyclerView.Adapter<flight_adapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.logoIv)
        val durationTv: TextView = itemView.findViewById(R.id.durationTv)
        val dateTv: TextView = itemView.findViewById(R.id.dateTv)
        val returnDateTv: TextView = itemView.findViewById(R.id.returnDateTv)
        val destinationTv: TextView = itemView.findViewById(R.id.destinationTv)
        val departureTv: TextView = itemView.findViewById(R.id.departureTv)
        val priceTv: TextView = itemView.findViewById(R.id.priceTv)
        val button: Button=itemView.findViewById(R.id.btnBooking)
    }

    fun setFilteredList(mList: ArrayList<flight_items_view_model>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val currentItem = mList[position]


        holder.logo.setImageResource(currentItem.logo)
        holder.durationTv.text = "${currentItem.duration}"
        holder.dateTv.text = "${currentItem.date}"
        holder.returnDateTv.text = "${currentItem.returnDate}"
        holder.destinationTv.text = "${currentItem.destination}"
        holder.departureTv.text = "${currentItem.departure}"
        holder.priceTv.text = "${currentItem.price} $"

        holder.button.setOnClickListener() {
            val intent = Intent(holder.itemView.context, BookingActivity::class.java)
            intent.putExtra("duration", mList[position].duration)
            intent.putExtra("date", mList[position].date)
            intent.putExtra("returnDate", mList[position].returnDate)
            intent.putExtra("destination", mList[position].destination)
            intent.putExtra("departure", mList[position].departure)
            intent.putExtra("price", mList[position].price)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
