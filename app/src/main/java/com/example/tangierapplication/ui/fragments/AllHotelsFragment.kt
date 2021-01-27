import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ratingapp.ui.HotelsAdapter
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_all_hotels.*

class AllHotelsFragment : Fragment(R.layout.fragment_all_hotels) {
    lateinit var hotelsAdapter: HotelsAdapter
    var db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference=db.collection("Hotels")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()

        hotelsAdapter?.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("hotel",it)
            }
            findNavController().navigate(
                R.id.action_allHotelsFragment_to_hotelFragment,bundle
            )
        }

    }


    override fun onStart() {
        super.onStart()
        hotelsAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        hotelsAdapter?.stopListening()
    }

    private fun setupRecyclerView(){
        val query:Query=collectionReference.orderBy("avgRating",Query.Direction.DESCENDING)
        val firestoreRecyclerOptions:FirestoreRecyclerOptions<Hotel> = FirestoreRecyclerOptions.Builder<Hotel>()
            .setQuery(query,Hotel::class.java)
            .build()
        hotelsAdapter = HotelsAdapter(firestoreRecyclerOptions)
        rvAllHotels.apply {
            adapter = hotelsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}