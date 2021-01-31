import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Places
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_all_hotels.*

class AllPlacesFragment : Fragment(R.layout.fragment_all_hotels) {
    lateinit var placesAdapter: PlacesAdapter
    var db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference=db.collection("Places")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        placesAdapter?.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("place",it)
            }
            findNavController().navigate(
                R.id.action_allPlacesFragment_to_placeFragment,bundle
            )
        }

    }


    override fun onStart() {
        super.onStart()
        placesAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        placesAdapter?.stopListening()
    }

    private fun setupRecyclerView(){
        val query:Query=collectionReference.orderBy("avgRating",Query.Direction.DESCENDING)
        val firestoreRecyclerOptions:FirestoreRecyclerOptions<Places> = FirestoreRecyclerOptions.Builder<Places>()
            .setQuery(query,Places::class.java)
            .build()
        placesAdapter = PlacesAdapter(firestoreRecyclerOptions)
        rvAllHotels.apply {
            adapter =placesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}