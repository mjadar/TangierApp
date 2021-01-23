package com.example.ratingapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import kotlinx.android.synthetic.main.item_hotel_preview.view.*

class HotelsAdapter(
    var hotels:List<Hotel>
) : RecyclerView.Adapter<HotelsAdapter.TodoViewHolder>(){

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_preview,parent,false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hotels.size
    }

    //bind data to our items , takes data from todos and
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.itemView.apply {
            tvTitle.text = hotels[position].title
            tvDescription.text = hotels[position].description
            tvAdresse.text = hotels[position].adresse
            ivHotelImage.setImageResource(hotels[position].image)
            setOnClickListener {
              onItemClickListener?.let{it(hotels[position])}
            }
        }
    }

    private var onItemClickListener: ((Hotel) -> Unit) ? = null

    fun setOnItemClickListener(listener: (Hotel) -> Unit) {
        onItemClickListener = listener
    }
}