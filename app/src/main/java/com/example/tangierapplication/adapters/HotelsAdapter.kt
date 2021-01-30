import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.fragment_hotel.view.*
import kotlinx.android.synthetic.main.item_hotel_preview.view.*

class HotelsAdapter(
    options: FirestoreRecyclerOptions<Hotel>
) : FirestoreRecyclerAdapter<Hotel,HotelsAdapter.TodoViewHolder>(options){

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_preview,parent,false)
        return TodoViewHolder(view)
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int, model: Hotel) {

        holder.itemView.apply {
            model.hotelId=snapshots.getSnapshot(position).id
            tvTitle.text = model.name
            tvNoteMoyenne.text = model.avgRating.toString()
            tvNoteNombre.text = model.numRating.toString()
            rbRatingBar.numStars = model.avgRating.toInt()
            Glide.with(ivHotelImage.context)
                    .load(model.pictures["main"])
                    .into(ivHotelImage)

            setOnClickListener {
                onItemClickListener?.let{it(model)}
            }
        }
    }

    private var onItemClickListener: ((Hotel) -> Unit) ? = null

    fun setOnItemClickListener(listener: (Hotel) -> Unit) {
        onItemClickListener = listener
    }

}