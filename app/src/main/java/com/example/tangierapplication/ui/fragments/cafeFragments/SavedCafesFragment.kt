import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Cafe
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Saved
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_saved_hotels.*


class SavedCafesFragment : Fragment(R.layout.fragment_saved_hotels) {
    lateinit var ScafesAdapter: SavedCafeAdapter
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userReference: CollectionReference =db.collection("Users").document(Firebase.auth.currentUser?.uid!!).collection("Favorites")
    private val collectionReference: CollectionReference = db.collection("Cafes")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialSetup()
        setupRecyclerView()
    }

    private fun initialSetup(){
        ScafesAdapter = SavedCafeAdapter(mutableListOf())
        rvSavedHotels.apply {
            adapter = ScafesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupRecyclerView(){
        var liste = mutableListOf<String>()
        var listeCafes = mutableListOf<Cafe>()
        userReference.whereEqualTo("type","cafe")
                .get()
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val fav = document.toObject(Saved::class.java)
                            liste.add(fav.documentRef)
                        }
                        if(liste.isEmpty()){
                            initialSetup()
                            return@addOnCompleteListener
                        }
                        collectionReference.whereIn(FieldPath.documentId(),liste).get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result!!) {
                                            var dc = document.toObject(Cafe::class.java)
                                            listeCafes.add(dc)
                                        }
                                        ScafesAdapter = SavedCafeAdapter(listeCafes)
                                        ScafesAdapter.notifyDataSetChanged()
                                        rvSavedHotels.apply {
                                            adapter = ScafesAdapter
                                            layoutManager = LinearLayoutManager(context)
                                        }
                                    }
                                }
                    }
                }
        var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition
                deleteFavCafe(listeCafes,position)
                listeCafes.removeAt(position)
                ScafesAdapter.notifyItemRemoved(position)
                ScafesAdapter.notifyDataSetChanged()
            }
        }
        val itemTouchHelper= ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rvSavedHotels)

    }

    private fun deleteFavCafe(listeCafe:MutableList<Cafe>,position:Int){
        userReference.whereEqualTo("documentRef",listeCafe.get(position).cafeId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                          userReference.document(document.id).delete()
                        }
                    }
            }
    }

}