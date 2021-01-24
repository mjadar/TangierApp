//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.ratingapp.ui.HotelsAdapter
//import com.example.tangierapplication.R
//import com.example.tangierapplication.db.DataHotelsFb
//import kotlinx.android.synthetic.main.fragment_saved_hotels.*
//
//class SavedHotelsFragment : Fragment(R.layout.fragment_saved_hotels) {
//    lateinit var hotelsAdapter: HotelsAdapter
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
//        hotelsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putString("title",it.name)
//                putString("type","saved")
//            }
//            findNavController().navigate(
//                R.id.action_savedHotelsFragment_to_hotelFragment,
//                bundle
//            )
//        }
//
//    }
//
//    private fun setupRecyclerView(){
//        hotelsAdapter = HotelsAdapter(DataHotelsFb.getSavedHotels())
//        rvSavedHotels.apply {
//            adapter = hotelsAdapter
//            layoutManager = LinearLayoutManager(activity)
//        }
//    }
//}