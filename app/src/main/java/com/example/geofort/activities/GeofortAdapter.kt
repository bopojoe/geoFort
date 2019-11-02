package com.example.geofort.activities



import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_geofort.view.*
import com.example.geofort.R
import com.example.geofort.helpers.readImageFromPath
import com.example.geofort.models.GeofortModel

interface GeofortListener {
    fun onGeofortClick(geofort: GeofortModel)
}

class GeofortAdapter constructor(
    private var geoforts: List<GeofortModel>,
    private val listener: GeofortListener
) : RecyclerView.Adapter<GeofortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_geofort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val geofort = geoforts[holder.adapterPosition]
        holder.bind(geofort, listener)
    }

    override fun getItemCount(): Int = geoforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(geofort: GeofortModel, listener: GeofortListener) {
            itemView.geofortTitle.text = geofort.title
            itemView.description.text = geofort.description
            itemView.geofortImageList.setImageBitmap(readImageFromPath(itemView.context, geofort.image))
            itemView.visited_card.isChecked = geofort.visited
            itemView.setOnClickListener { listener.onGeofortClick(geofort) }
        }
    }
}