import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tangierapplication.R
import com.example.tangierapplication.db.DataHotelsFb
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Review
import com.example.tangierapplication.models.Saved
import com.example.tangierapplication.ui.dialogs.CustomDialogFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_all_hotels.*
import kotlinx.android.synthetic.main.fragment_hotel.*


class HotelFragment : Fragment(R.layout.fragment_hotel), CustomDialogFragment.RatingListener  {

    private var db: FirebaseFirestore= FirebaseFirestore.getInstance()
    private lateinit var hotelRef: DocumentReference
    private lateinit var hotelId:String
    private lateinit var commentAdapter:CommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hotel = arguments?.getParcelable<Hotel>("hotel")
        if (hotel != null ) {
            hotelId = hotel.hotelId  ?: throw IllegalArgumentException("Must have hotelId")
            hotelRef = db.collection("Hotels").document(hotelId)
            setData(hotel)
        }
        getReview()
        setupRecyclerView()
        btnRateUs.setOnClickListener {
            var dialog = CustomDialogFragment()
            dialog.setTargetFragment(this,1);
            dialog.show(this.parentFragmentManager,"CustomDialogFragment")
        }

        btnFavorite.setOnClickListener {
            addFavorite(hotel!!)
        }
    }

    fun setData(hotel:Hotel){
//            ivHhotelImage.setImageResource(hotelSelected.image)
            tvHtitle.setText(hotel.name)
            tvHadresse.setText(hotel.adresse)
            third_ratingbar.rating = hotel.avgRating
            third_rating_number.setText(hotel.avgRating.toString())
            third_rating_number2.setText(hotel.numRating.toString())
            about_text.setText(hotel.description)
    }

    private fun addFavorite(hotel:Hotel){
        val userRef = db.collection("Users").document(Firebase.auth.currentUser?.uid!!)
        val savedRef = userRef.collection("Favorites").document()
        // In a transaction, add the new rating and update the aggregate totals
       Firebase.firestore.runTransaction { transaction ->
           val fav = Saved("hotel",hotelId)
            // Commit to Firestore
            transaction.set(savedRef,fav)
            null
        } .addOnSuccessListener {
            Log.d("MainActivity", "Added to favorite")
            Toast.makeText(context, "Added to favorite", Toast.LENGTH_SHORT).show()
          }.addOnFailureListener { e ->
            Log.w("MainActivity", "Add Favorite failed", e)
            Toast.makeText(parentFragment?.context, "Add Favorite failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addRating(hotelRef: DocumentReference, rating: Review): Task<Void> {
        // Create reference for new rating, for use inside the transaction
        val ratingRef = hotelRef.collection("ratings_hotels").document()
        // In a transaction, add the new rating and update the aggregate totals
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

    private fun setupRecyclerView(){
        val query: Query =hotelRef.collection("ratings_hotels")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Review> = FirestoreRecyclerOptions.Builder<Review>()
                .setQuery(query,Review::class.java)
                .build()
        commentAdapter = CommentAdapter(firestoreRecyclerOptions)
        rvAllComments.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    override fun onStart() {
        super.onStart()
        commentAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        commentAdapter?.stopListening()
    }


    fun getReview() {
        val query:Query= DataHotelsFb.collectionReference
        val frc = FirestoreRecyclerOptions.Builder<Review>()
                .setQuery(query,Review::class.java)
                .build()
                .snapshots
        for(hotel in frc){
                println("haaaaahowaaaaaaaaaaaaaaaaaaa "+ hotel)
            }
    }

}