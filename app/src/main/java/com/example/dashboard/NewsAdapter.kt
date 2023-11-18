package com.example.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.lang.String
import kotlin.Int


class NewsAdapter(var listDomains: ArrayList<ListDomain>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.feederName.setText(String.valueOf(listDomains[position].getTitle1()))
        val drawableResourceId = holder.itemView.context.resources.getIdentifier(
            listDomains[position].getUrl1(), "drawable", holder.itemView.context.packageName
        )
        Glide.with(holder.itemView.context)
            .load(drawableResourceId)
            .into(holder.removeItem)
    }

    override fun getItemCount(): Int {
        return listDomains.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var feederName: TextView
        var removeItem: ImageView

        init {
            feederName = itemView.findViewById(R.id.feederName)
            removeItem = itemView.findViewById(R.id.removeFeeder)
        }
    }
}
