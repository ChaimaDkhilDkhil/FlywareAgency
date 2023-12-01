package com.example.dashboard

import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class TransportsAdapter(private val dataList: ArrayList<Transports>) : RecyclerView.Adapter<TransportsAdapter.ViewHolderClass>()  {

    var onItemClick: ((Transports) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transports_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.img)
        holder.rvName.text = currentItem.transport.name
        holder.rvLocation.text = currentItem.transport.location
        holder.rvPrice.text = currentItem.transport.price
        holder.button.setOnClickListener() {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("image", dataList[position].img)
            intent.putExtra("pays", dataList[position].pays)
            intent.putExtra("name", dataList[position].transport.name)
            intent.putExtra("location", dataList[position].transport.location)
            intent.putExtra("price", dataList[position].transport.price)
            intent.putExtra("description", dataList[position].transport.description)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val rvImage: ImageView = itemView.findViewById(R.id.transportImage)
        val rvName: TextView = itemView.findViewById(R.id.transportName)
        val rvLocation: TextView = itemView.findViewById(R.id.hotelLocation)
        val rvPrice: TextView = itemView.findViewById(R.id.transportPrice)
        val button: Button = itemView.findViewById(R.id.btnTransportBooking)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val selectedCountry = dataList[position]
                val intent = Intent(v?.context, Detail_transportActivity::class.java)
                intent.putExtra("countryName", selectedCountry.pays)
                v?.context?.startActivity(intent)
            }
        }
    }





}