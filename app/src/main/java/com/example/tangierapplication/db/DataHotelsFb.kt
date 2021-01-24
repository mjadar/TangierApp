package com.example.tangierapplication.db

import android.util.Log
import com.example.tangierapplication.models.Hotel
import com.example.tangierapplication.models.Review
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class DataHotelsFb {

    companion object {
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference =db.collection("Hotels")
        val instance = DataHotelsFb()
        var myHotels = mutableListOf<Hotel>()

        var mySavedHotels = mutableListOf<Hotel>()

//        fun getAllHotels():MutableList<Hotel>{
//            return myHotels
//        }

        fun getSavedHotels():MutableList<Hotel>{
            return mySavedHotels
        }

        fun getHotel(titre: String): Hotel? {
            var hot: Hotel? =null
            val query:Query=collectionReference
            val frc = FirestoreRecyclerOptions.Builder<Hotel>()
                .setQuery(query,Hotel::class.java)
                .build()
                .snapshots

                for(hotel in frc){
                    if (hotel.name == titre){
                        hot=hotel
                        println("haaaaahowaaaaaaaaaaaaaaaaaaa"+hot)
                    }
                }
            return hot
        }

        fun getSearch(titre : String):MutableList<Hotel>{
            var list = mutableListOf<Hotel>()
            for(hotel in myHotels){
                if(hotel.name==titre){
                    list.add(hotel)
                }
            }
            return list
        }



         fun addReview(HotelId: String,review:Review){
            val hotel = db.collection("Hotels").document(HotelId)
            val newReview = hotel.collection("ratings_hotels")
            Firebase.firestore.runTransaction{ transaction ->
                transaction.get(hotel).data?.let {
                    val oldRating = transaction.get(hotel).data!!["avgRating"].toString().toFloat()
                    val oldNumRating = transaction.get(hotel).data!!["numRating"].toString().toFloat().toInt()
                    val newNumRating = oldNumRating+1
                    val newRating = (oldRating* oldNumRating+review.rating)/newNumRating
                    transaction.update(hotel,"avgRating",newRating,"numRating",newNumRating)
                    newReview.add(review)
                }
                null
            }
        }

        private fun addHotel(hotel :Hotel)= CoroutineScope(Dispatchers.IO).launch{
            db.collection("Hotels")
                    .add(hotel)
                    .addOnSuccessListener { documentReference -> Log.d("MainActivity", "DocumentSnapshot added with ID: " + documentReference.id) }
                    .addOnFailureListener { e -> Log.w("MainActivity", "Error adding document", e) }
                    .await()
        }

        private suspend fun getReviewsFromFirestore(RestaurantId:String):List<DocumentSnapshot>{
            val hotel = db.collection("Hotels").document(RestaurantId)
            val reviews = hotel.collection("ratings_hotels")
            val snap = reviews.orderBy("rating",Query.Direction.DESCENDING)
                    .get().await()
            return snap.documents
        }

        private suspend fun getReviews(hotelId:String):MutableList<Review>{
            var maListe= mutableListOf<Review>()
            try{
                val querySnap= getReviewsFromFirestore(hotelId)
                for(document in querySnap){
                    val rest = document.toObject(Review::class.java)
                    if (rest != null) {
                        maListe.add(rest)
                    }
                }
            }catch (e:Exception){
                Log.d("MainActivity","erroorrrrrr")
            }
            return maListe
        }



    }
}


