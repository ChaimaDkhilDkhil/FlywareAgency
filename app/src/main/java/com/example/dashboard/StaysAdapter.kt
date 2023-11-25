package com.example.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StaysAdapter(private val dataList: ArrayList<Hotels>) : RecyclerView.Adapter<StaysAdapter.ViewHolderClass>() {
    var onItemClick: ((Hotels) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stays_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.img)
        holder.rvName.text = currentItem.hotel.name
        holder.rvLocation.text = currentItem.hotel.location
        holder.rvPrice.text = currentItem.hotel.price
        holder.button.setOnClickListener() {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("image", dataList[position].img)
            intent.putExtra("pays", dataList[position].pays)
            intent.putExtra("name", dataList[position].hotel.name)
            intent.putExtra("location", dataList[position].hotel.location)
            intent.putExtra("price", dataList[position].hotel.price)
            intent.putExtra("description", dataList[position].hotel.description)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val rvImage: ImageView = itemView.findViewById(R.id.hotelImage)
        val rvName: TextView = itemView.findViewById(R.id.hotelName)
        val rvLocation: TextView = itemView.findViewById(R.id.hotelLocation)
        val rvPrice: TextView = itemView.findViewById(R.id.hotelPrice)
        val button: Button = itemView.findViewById(R.id.btnHotelBooking)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val selectedCountry = dataList[position]
                val intent = Intent(v?.context, DetailActivity::class.java)
                intent.putExtra("countryName", selectedCountry.pays)
                v?.context?.startActivity(intent)
            }
        }
    }
}
