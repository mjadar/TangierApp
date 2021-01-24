import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.example.tangierapplication.R
import com.example.tangierapplication.db.DataHotelsFb
import com.example.tangierapplication.models.Hotel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.fragment_hotel.*

class HotelFragment : Fragment(R.layout.fragment_hotel) {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val collectionReference: CollectionReference =db.collection("Hotels")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hotel = arguments?.getParcelable<Hotel>("hotel")
        if (hotel != null ) {
            setData(hotel)
        }



        rbRating.setOnRatingBarChangeListener((object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                tvHadresse.setText("Rating is $p1")
            }
        }))

    }

    fun setData(hotel:Hotel){

//            ivHhotelImage.setImageResource(hotelSelected.image)
            tvHtitle.setText(hotel.name)
            tvHadresse.setText(hotel.adresse)
            third_ratingbar.rating = hotel.avgRating
            third_rating_number.setText(hotel.avgRating.toString())
            third_rating_number2.setText(hotel.numRating.toString())
            about_text.setText(hotel.description)
//            type_of_view_text

    }


}