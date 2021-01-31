import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Restaurant
import kotlinx.android.synthetic.main.item_hotel_preview.view.*

class SavedRestaurantAdapter(
        var liste:MutableList<Restaurant>
) : RecyclerView.Adapter<SavedRestaurantAdapter.TodoViewHolder>(){

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_preview,parent,false)
        return TodoViewHolder(view)
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.itemView.apply {
            tvTitle.text = liste[position].name
            tvNoteMoyenne.text = liste[position].avgRating.toString()
            tvNoteNombre.text = liste[position].numRating.toString()
            rbRatingBar.numStars = liste[position].avgRating.toInt()
            Glide.with(ivHotelImage.context)
                .load(liste[position].pictures["main"])
                .into(ivHotelImage)

            setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("restaurant",liste[position])
                }
                findNavController().navigate(
                        R.id.action_savedRestaurantsFragment_to_restaurantFragment,
                        bundle
                )
            }
        }
    }


    override fun getItemCount(): Int {
        return liste.size
    }

}
