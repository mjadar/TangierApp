import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Places
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
import kotlinx.android.synthetic.main.fragment_hotel.*



class PlaceFragment : Fragment(R.layout.fragment_hotel), CustomDialogFragment.RatingListener  {

    private var db: FirebaseFirestore= FirebaseFirestore.getInstance()
    private lateinit var placeRef: DocumentReference
    private lateinit var placeId:String
    private lateinit var commentAdapter:CommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val place = arguments?.getParcelable<Places>("place")
        if (place != null ) {
            placeId = place.placeId  ?: throw IllegalArgumentException("Must have hotelId")
            placeRef = db.collection("Places").document(placeId)
            setData(place)
        }
        setupRecyclerView()
        btnRateUs.setOnClickListener {
            var dialog = CustomDialogFragment()
            dialog.setTargetFragment(this,1);
            dialog.show(this.parentFragmentManager,"CustomDialogFragment")
        }

        btnFavorite.setOnClickListener {
            addFavorite(place!!)
        }
    }

    fun setData(place:Places){
//            ivHhotelImage.setImageResource(hotelSelected.image)
            tvHtitle.setText(place.name)
            tvHadresse.setText(place.adresse)
            third_ratingbar.rating = place.avgRating
            third_rating_number.setText(place.avgRating.toString())
            third_rating_number2.setText(place.numRating.toString())
            about_text.setText(place.description)
        Glide.with(ivHhotelImage.context)
            .load(place.pictures["main"])
            .into(ivHhotelImage)
    }

    private fun addFavorite(place:Places){
        val userRef = db.collection("Users").document(Firebase.auth.currentUser?.uid!!)
        val savedRef = userRef.collection("Favorites").document()
        // In a transaction, add the new rating and update the aggregate totals
       Firebase.firestore.runTransaction { transaction ->
           val fav = Saved("place",placeId)
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

    private fun addRating(placeRef: DocumentReference, rating: Review): Task<Void> {
        // Create reference for new rating, for use inside the transaction
        val ratingRef = placeRef.collection("ratings_places").document()
        // In a transaction, add the new rating and update the aggregate totals
        return Firebase.firestore.runTransaction { transaction ->
            val place = transaction.get(placeRef).toObject<Places>()
            if (place == null) {
                throw Exception("Hotel not found at ${placeRef.path}")
            }
            // Compute new number of ratings
            val newNumRating = place.numRating + 1
            // Compute new average rating
            val oldRatingTotal = place.avgRating * place.numRating
            val newAvgRating = (oldRatingTotal + rating.rating) / newNumRating
            // Set new restaurant info
            place.numRating = newNumRating
            place.avgRating = newAvgRating
            // Commit to Firestore
            transaction.set(placeRef, place)
            transaction.set(ratingRef, rating)
            null
        }
    }

    override fun onRating(rating: Review) {
        addRating(placeRef, rating)
                .addOnSuccessListener {
                    Log.d("MainActivity", "Rating added")
                }
                .addOnFailureListener { e ->
                    Log.w("MainAcitivity", "Add rating failed", e)
                }
    }

    private fun setupRecyclerView(){
        val query: Query = placeRef.collection("ratings_places")
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

}