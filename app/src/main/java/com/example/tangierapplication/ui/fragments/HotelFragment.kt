import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Review
import com.example.tangierapplication.ui.dialogs.CustomDialogFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_hotel.*


class HotelFragment : Fragment(R.layout.fragment_hotel), CustomDialogFragment.RatingListener  {

    private var db: FirebaseFirestore= FirebaseFirestore.getInstance()
    private lateinit var hotelRef: DocumentReference
    private lateinit var hotelId:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hotel = arguments?.getParcelable<Hotel>("hotel")
        if (hotel != null ) {
            hotelId = hotel.hotelId  ?: throw IllegalArgumentException("Must have hotelId")
            hotelRef = db.collection("Hotels").document(hotelId)
            setData(hotel)
        }

        btnRateUs.setOnClickListener {
            var dialog = CustomDialogFragment()
            dialog.setTargetFragment(this,1);
            dialog.show(this.parentFragmentManager,"CustomDialogFragment")

        }


    }

    fun setData(hotel:Hotel){
//            ivHhotelImage.setImageResource(hotelSelected.image)
//            tvHtitle.setText(hotel.hotelId)
            tvHtitle.setText(hotel.name)
            tvHadresse.setText(hotel.adresse)
            third_ratingbar.rating = hotel.avgRating
            third_rating_number.setText(hotel.avgRating.toString())
            third_rating_number2.setText(hotel.numRating.toString())
            about_text.setText(hotel.description)
    }

    private fun addRating(hotelRef: DocumentReference, rating: Review): Task<Void> {
        // Create reference for new rating, for use inside the transaction
        val ratingRef = hotelRef.collection("ratings_hotels").document()
        // In a transaction, add the new rating and update the aggregate totals
        Log.d("Rating","ana f addRating")
        return Firebase.firestore.runTransaction { transaction ->
            val hotel = transaction.get(hotelRef).toObject<Hotel>()
            if (hotel == null) {
                throw Exception("Hotel not found at ${hotelRef.path}")
            }
            // Compute new number of ratings
            val newNumRating = hotel.numRating + 1
            // Compute new average rating
            val oldRatingTotal = hotel.avgRating * hotel.numRating
            val newAvgRating = (oldRatingTotal + rating.rating) / newNumRating
            // Set new restaurant info
            hotel.numRating = newNumRating
            hotel.avgRating = newAvgRating
            // Commit to Firestore
            transaction.set(hotelRef, hotel)
            transaction.set(ratingRef, rating)
            null
        }
    }

    override fun onRating(rating: Review) {
        addRating(hotelRef, rating)
                .addOnSuccessListener {
                    Log.d("MainActivity", "Rating added")
                }
                .addOnFailureListener { e ->
                    Log.w("MainAcitivity", "Add rating failed", e)
                }
    }

}