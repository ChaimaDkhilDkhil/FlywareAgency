package com.example.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClassTransport (private val dataList: ArrayList<Transports>) : RecyclerView.Adapter<AdapterClassTransport.ViewHolderClass>() {
    var onItemClick: ((Transports) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_transport_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.img)
        holder.rvTitle.text = currentItem.pays
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val rvImage: ImageView = itemView.findViewById(R.id.image)
        val rvTitle: TextView = itemView.findViewById(R.id.transportName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val selectedCountry = dataList[position]
                val intent = Intent(v?.context, TransportsListActivity::class.java)
                intent.putExtra("countryName", selectedCountry.pays)
                v?.context?.startActivity(intent)
            }
        }
    }
}