import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Saved
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_saved_hotels.*


class SavedHotelsFragment : Fragment(R.layout.fragment_saved_hotels) {
    lateinit var ShotelsAdapter: SavedHotelAdapter
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userReference: CollectionReference =db.collection("Users").document(Firebase.auth.currentUser?.uid!!).collection("Favorites")
    private val collectionReference: CollectionReference = db.collection("Hotels")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialSetup()
        setupRecyclerView()
//        ShotelsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putParcelable("hotel",it)
//            }
//            findNavController().navigate(
//                    R.id.action_savedHotelsFragment_to_hotelFragment,
//                    bundle
//            )
//        }
    }

    private fun initialSetup(){
        ShotelsAdapter = SavedHotelAdapter(mutableListOf())
        rvSavedHotels.apply {
            adapter = ShotelsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    private fun setupRecyclerView(){
        var liste = mutableListOf<String>()
        var listeHotels = mutableListOf<Hotel>()
        userReference.whereEqualTo("type","hotel")
                .get()
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val fav = document.toObject(Saved::class.java)
                            liste.add(fav.documentRef)
                        }
                        collectionReference.whereIn(FieldPath.documentId(),liste).get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result!!) {
                                            var dc = document.toObject(Hotel::class.java)
                                            listeHotels.add(dc)
                                        }
                                        ShotelsAdapter = SavedHotelAdapter(listeHotels)
                                        ShotelsAdapter.notifyDataSetChanged()
                                        rvSavedHotels.apply {
                                            adapter = ShotelsAdapter
                                            layoutManager = LinearLayoutManager(context)
                                        }
                                    }
                                }

                    }
                }

    }

}