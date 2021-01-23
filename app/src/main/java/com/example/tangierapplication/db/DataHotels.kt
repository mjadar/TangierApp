package com.example.tangierapplication.db

import com.example.tangierapplication.R
import com.example.tangierapplication.models.Hotel

class DataHotels {
    companion object {
        val instance = DataHotels()
        val myHotels = mutableListOf(
        Hotel("hotel1","hotel marocain","CASABLANCA","today", R.drawable.image1),
        Hotel("hotel2","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel3","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel4","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel5","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel6","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel7","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel8","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel9","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel10","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("hotel11","hotel marocain","CASABLANCA","today",R.drawable.image1)
        )

        val mySavedHotels = mutableListOf(
        Hotel("savedHotel1","hotel marocain","CASABLANCA","today", R.drawable.image1),
        Hotel("savedHotel2","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel3","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel4","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel5","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel6","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel7","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel8","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel9","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel10","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel11","hotel marocain","CASABLANCA","today",R.drawable.image1),
        Hotel("savedHotel12","hotel marocain","CASABLANCA","today",R.drawable.image1)
        )

        fun getAllHotels():MutableList<Hotel>{
            return myHotels
        }

        fun getSavedHotels():MutableList<Hotel>{
            return mySavedHotels
        }

        fun getHotel(titre: String,type:String): Hotel? {
            if(type=="all")
                for(hotel in myHotels){
                    if (hotel.title == titre){
                        return hotel
                    }
                }
            else{
                for(hotel in mySavedHotels){
                    if (hotel.title == titre){
                        return hotel
                    }
                }
            }
            return null
        }

        fun getSearch(titre : String):MutableList<Hotel>{
            var list = mutableListOf<Hotel>()
            for(hotel in myHotels){
                if(hotel.title==titre){
                    list.add(hotel)
                }
            }
            return list
        }

    }
}