import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Cafe
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


class CafeFragment : Fragment(R.layout.fragment_hotel), CustomDialogFragment.RatingListener  {

    private var db: FirebaseFirestore= FirebaseFirestore.getInstance()
    private lateinit var cafeRef: DocumentReference
    private lateinit var cafeId:String
    private lateinit var commentAdapter:CommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cafe = arguments?.getParcelable<Cafe>("cafe")
        if (cafe != null ) {
            cafeId = cafe.cafeId  ?: throw IllegalArgumentException("Must have hotelId")
            cafeRef = db.collection("Cafes").document(cafeId)
            setData(cafe)
        }
        setupRecyclerView()
        btnRateUs.setOnClickListener {
            var dialog = CustomDialogFragment()
            dialog.setTargetFragment(this,1);
            dialog.show(this.parentFragmentManager,"CustomDialogFragment")
        }

        btnFavorite.setOnClickListener {
            addFavorite(cafe!!)
        }
    }

    fun setData(cafe: Cafe){
//            ivHhotelImage.setImageResource(hotelSelected.image)
            tvHtitle.setText(cafe.name)
            tvHadresse.setText(cafe.adresse)
            third_ratingbar.rating = cafe.avgRating
            third_rating_number.setText(cafe.avgRating.toString())
            third_rating_number2.setText(cafe.numRating.toString())
            about_text.setText(cafe.description)
        Glide.with(ivHhotelImage.context)
            .load(cafe.pictures["main"])
            .into(ivHhotelImage)
    }

    private fun addFavorite(cafe:Cafe){
        val userRef = db.collection("Users").document(Firebase.auth.currentUser?.uid!!)
        val savedRef = userRef.collection("Favorites").document()
        // In a transaction, add the new rating and update the aggregate totals
       Firebase.firestore.runTransaction { transaction ->
           val fav = Saved("cafe",cafeId)
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

    private fun addRating(cafeRef: DocumentReference, rating: Review): Task<Void> {
        // Create reference for new rating, for use inside the transaction
        val ratingRef = cafeRef.collection("ratings_cafes").document()
        // In a transaction, add the new rating and update the aggregate totals
        return Firebase.firestore.runTransaction { transaction ->
            val cafe = transaction.get(cafeRef).toObject<Cafe>()
            if (cafe == null) {
                throw Exception("Cafe not found at ${cafeRef.path}")
            }
            // Compute new number of ratings
            val newNumRating = cafe.numRating + 1
            // Compute new average rating
            val oldRatingTotal = cafe.avgRating * cafe.numRating
            val newAvgRating = (oldRatingTotal + rating.rating) / newNumRating
            // Set new restaurant info
            cafe.numRating = newNumRating
            cafe.avgRating = newAvgRating
            // Commit to Firestore
            transaction.set(cafeRef, cafe)
            transaction.set(ratingRef, rating)
            null
        }
    }

    override fun onRating(rating: Review) {
        addRating(cafeRef, rating)
                .addOnSuccessListener {
                    Log.d("MainActivity", "Rating added")
                }
                .addOnFailureListener { e ->
                    Log.w("MainAcitivity", "Add rating failed", e)
                }
    }

    private fun setupRecyclerView(){
        val query: Query =cafeRef.collection("ratings_cafes")
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