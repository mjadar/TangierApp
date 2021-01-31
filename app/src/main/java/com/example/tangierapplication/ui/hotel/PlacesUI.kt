package com.example.tangierapplication.ui.hotel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tangierapplication.R
import kotlinx.android.synthetic.main.hotels_main.*
import kotlinx.android.synthetic.main.places_main.*
import kotlinx.android.synthetic.main.restaurant_main.*

class PlacesUI: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.places_main)
        bottomNavigationViewPlace.setupWithNavController(placesNavHostFragment.findNavController())
    }
}