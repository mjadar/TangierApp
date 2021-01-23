import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.example.tangierapplication.R
import com.example.tangierapplication.db.DataHotels
import kotlinx.android.synthetic.main.fragment_hotel.*

class HotelFragment : Fragment(R.layout.fragment_hotel) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleHotel = arguments?.getString("title")
        val type       = arguments?.getString("type")
        if (titleHotel != null && type != null) {
            setData(titleHotel,type)
        }
        rbRating.setOnRatingBarChangeListener((object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                tvHadresse.setText("Rating is $p1")
            }
        }))
    }

    fun setData(title : String,type:String){
        val hotelSelected = DataHotels.getHotel(title,type)!!
        hotelSelected?.let {
            ivHhotelImage.setImageResource(hotelSelected.image)
            tvHtitle.setText(hotelSelected.title)
            tvHadresse.setText(hotelSelected.adresse)
        }
    }


}