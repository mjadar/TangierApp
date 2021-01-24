//import android.os.Bundle
//import android.view.View
//import androidx.core.widget.addTextChangedListener
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.ratingapp.ui.HotelsAdapter
//import com.example.tangierapplication.R
//import com.example.tangierapplication.db.DataHotelsFb
//import kotlinx.android.synthetic.main.fragment_search_hotels.*
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class SearchHotelsFragment : Fragment(R.layout.fragment_search_hotels) {
//
//    lateinit var hotelsAdapter: HotelsAdapter
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        var job : Job? = null
//
//        etSearch.addTextChangedListener { editable ->
//            job?.cancel()
//            job = MainScope().launch {
//                delay(1000)
//                editable?.let {
//                    if(editable.toString().isNotEmpty()){
//                        setupRecyclerView(editable.toString())
//                    }
//                }
//            }
//        }
//        hotelsAdapter.setOnItemClickListener {
//            findNavController().navigate(
//                R.id.action_searchHotelsFragment_to_hotelFragment
//            )
//        }
//    }
//
//
//    private fun setupRecyclerView(title :String){
//        hotelsAdapter = HotelsAdapter(DataHotelsFb.getSearch(title))
//        rvSearchHotels.apply {
//            adapter = hotelsAdapter
//            layoutManager = LinearLayoutManager(activity)
//        }
//    }
//}