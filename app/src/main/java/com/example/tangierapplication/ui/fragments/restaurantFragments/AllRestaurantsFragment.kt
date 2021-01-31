import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Restaurant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_all_hotels.*

class AllRestaurantsFragment : Fragment(R.layout.fragment_all_hotels) {
    lateinit var restAdapter: RestaurantsAdapter
    var db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference=db.collection("Restaurants")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        restAdapter?.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("restaurant",it)
            }
            findNavController().navigate(
                R.id.action_allRestaurantsFragment_to_restaurantFragment,bundle
            )
        }

    }


    override fun onStart() {
        super.onStart()
        restAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        restAdapter?.stopListening()
    }

    private fun setupRecyclerView(){
        val query:Query=collectionReference.orderBy("avgRating",Query.Direction.DESCENDING)
        val firestoreRecyclerOptions:FirestoreRecyclerOptions<Restaurant> = FirestoreRecyclerOptions.Builder<Restaurant>()
            .setQuery(query,Restaurant::class.java)
            .build()
        restAdapter = RestaurantsAdapter(firestoreRecyclerOptions)
        rvAllHotels.apply {
            adapter =  restAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}