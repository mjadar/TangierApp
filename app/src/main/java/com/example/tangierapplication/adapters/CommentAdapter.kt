import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Review
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_rating.view.*
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(
options: FirestoreRecyclerOptions<Review>
) : FirestoreRecyclerAdapter<Review, CommentAdapter.TodoViewHolder>(options) {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int, model: Review) {
        holder.itemView.apply {
            ratingItemName.text = model.userName
            ratingItemText.text = model.textR
            ratingItemRating.rating = model.rating
            if(model.timestamp!=null)
                 ratingItemDate.text = FORMAT.format(model.timestamp)
//            ivHotelImage.setImageResource(hotels[position].image)
        }
    }

    companion object {
        private val FORMAT = SimpleDateFormat(
                "MM/dd/yyyy", Locale.US)
    }

}