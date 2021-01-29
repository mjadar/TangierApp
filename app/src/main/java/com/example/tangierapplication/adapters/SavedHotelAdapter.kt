import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_hotel_preview.view.*

class SavedHotelAdapter(
        var liste:MutableList<Hotel>
) : RecyclerView.Adapter<SavedHotelAdapter.TodoViewHolder>(){

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
//            ivHotelImage.setImageResource(hotels[position].image)
            setOnClickListener {
                onItemClickListener?.let{it(liste[position])}
            }
        }
    }

    private var onItemClickListener: ((Hotel) -> Unit) ? = null

    fun setOnItemClickListener(listener: (Hotel) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return liste.size
    }

}
