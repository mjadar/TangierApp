import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Cafe
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_all_hotels.*

class AllCafesFragment : Fragment(R.layout.fragment_all_hotels) {
    lateinit var cafesAdapter: CafesAdapter
    var db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference=db.collection("Cafes")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        cafesAdapter?.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("cafe",it)
            }
            findNavController().navigate(
                R.id.action_allCafesFragment_to_cafeFragment,bundle
            )
        }

    }


    override fun onStart() {
        super.onStart()
        cafesAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        cafesAdapter?.stopListening()
    }

    private fun setupRecyclerView(){
        val query:Query=collectionReference.orderBy("avgRating",Query.Direction.DESCENDING)
        val firestoreRecyclerOptions:FirestoreRecyclerOptions<Cafe> = FirestoreRecyclerOptions.Builder<Cafe>()
            .setQuery(query,Cafe::class.java)
            .build()
        cafesAdapter = CafesAdapter(firestoreRecyclerOptions)
        rvAllHotels.apply {
            adapter = cafesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}