import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Restaurant
import com.example.tangierapplication.models.Review
import com.example.tangierapplication.models.Saved
import com.example.tangierapplication.ui.fragments.dialogs.CustomDialogFragment
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


class RestaurantFragment : Fragment(R.layout.fragment_hotel), CustomDialogFragment.RatingListener  {

    private var db: FirebaseFirestore= FirebaseFirestore.getInstance()
    private lateinit var restRef: DocumentReference
    private lateinit var restId:String
    private lateinit var commentAdapter:CommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rest = arguments?.getParcelable<Restaurant>("restaurant")
        if (rest != null ) {
            restId = rest.restaurantId  ?: throw IllegalArgumentException("Must have hotelId")
            restRef = db.collection("Restaurants").document(restId)
            setData(rest)
        }

        setupRecyclerView()
        btnRateUs.setOnClickListener {
            var dialog = CustomDialogFragment()
            dialog.setTargetFragment(this,1);
            dialog.show(this.parentFragmentManager,"CustomDialogFragment")
        }

        btnFavorite.setOnClickListener {
            addFavorite(rest!!)
        }
    }

    fun setData(restau:Restaurant){
//            ivHhotelImage.setImageResource(hotelSelected.image)
            tvHtitle.setText(restau.name)
            tvHadresse.setText(restau.adresse)
            third_ratingbar.rating = restau.avgRating
            third_rating_number.setText(restau.avgRating.toString())
            third_rating_number2.setText(restau.numRating.toString())
            about_text.setText(restau.description)
        Glide.with(ivHhotelImage.context)
            .load(restau.pictures["main"])
            .into(ivHhotelImage)
    }

    private fun addFavorite(restau:Restaurant){
        val userRef = db.collection("Users").document(Firebase.auth.currentUser?.uid!!)
        val savedRef = userRef.collection("Favorites").document()
        // In a transaction, add the new rating and update the aggregate totals
       Firebase.firestore.runTransaction { transaction ->
           val fav = Saved("rest",restId)
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

    private fun addRating(restaurantRef: DocumentReference, rating: Review): Task<Void> {
        // Create reference for new rating, for use inside the transaction
        val ratingRef = restaurantRef.collection("ratings_restaurant").document()
        // In a transaction, add the new rating and update the aggregate totals
        return Firebase.firestore.runTransaction { transaction ->
            val restau = transaction.get(restaurantRef).toObject<Restaurant>()
            if (restau == null) {
                throw Exception("Hotel not found at ${restaurantRef.path}")
            }
            // Compute new number of ratings
            val newNumRating = restau.numRating + 1
            // Compute new average rating
            val oldRatingTotal = restau.avgRating * restau.numRating
            val newAvgRating = (oldRatingTotal + rating.rating) / newNumRating
            // Set new restaurant info
            restau.numRating = newNumRating
            restau.avgRating = newAvgRating
            // Commit to Firestore
            transaction.set(restaurantRef, restau)
            transaction.set(ratingRef, rating)
            null
        }
    }

    override fun onRating(rating: Review) {
        addRating(restRef, rating)
                .addOnSuccessListener {
                    Log.d("MainActivity", "Rating added")
                }
                .addOnFailureListener { e ->
                    Log.w("MainActivity", "Add rating failed", e)
                }
    }

    private fun setupRecyclerView(){
        val query: Query =restRef.collection("ratings_restaurant")
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