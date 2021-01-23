import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ratingapp.ui.HotelsAdapter
import com.example.tangierapplication.R
import com.example.tangierapplication.db.DataHotels
import kotlinx.android.synthetic.main.fragment_all_hotels.*

class AllHotelsFragment : Fragment(R.layout.fragment_all_hotels) {
    lateinit var hotelsAdapter: HotelsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        hotelsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("title",it.title)
                putString("type","all")
            }
            findNavController().navigate(
                R.id.action_allHotelsFragment_to_hotelFragment,bundle
            )
        }

    }

    private fun setupRecyclerView(){
        hotelsAdapter = HotelsAdapter(DataHotels.getAllHotels())
        rvAllHotels.apply {
            adapter = hotelsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}