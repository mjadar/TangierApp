package com.example.tangierapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.tangierapplication.R
import com.example.tangierapplication.models.User
import com.example.tangierapplication.ui.hotel.CafesUI
import com.example.tangierapplication.ui.hotel.HotelsUI
import com.example.tangierapplication.ui.hotel.PlacesUI
import com.example.tangierapplication.ui.hotel.RestaurantsUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity() {
    var auth: FirebaseAuth=FirebaseAuth.getInstance()
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setDisplayName()
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(baseContext,"You have been signed out !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
        val pic = listOf(R.drawable.xxx, R.drawable.kes, R.drawable.tangerhh)

        for(image in pic )
        {
            flipperImages(image)
        }
        rdHotels.setOnClickListener {
            Intent(this, HotelsUI::class.java).also {
                startActivity(it)
            }
        }
        rdCafe.setOnClickListener {
            Intent(this,CafesUI::class.java).also {
                startActivity(it)
            }
        }
        rdPlacesVisite.setOnClickListener {
            Intent(this, PlacesUI::class.java).also {
                startActivity(it)
            }
        }
        rdRestaurants.setOnClickListener {
            Intent(this, RestaurantsUI::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setDisplayName(){
        db.collection("Users")
            .whereEqualTo("uid", auth.currentUser?.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        var user = document.toObject(User::class.java)
                        nameUser.setText("Hi, "+document.data["name"])
                    }

                } else {
                    Log.w(
                        "mainActivity",
                        "Error getting documents.",
                        task.exception
                    )
                }
            }

    }
    private fun flipperImages(image : Int)
    {
        val imageView = ImageView(this)
        imageView.setBackgroundResource(image)
        v_Flipper.addView(imageView)
        v_Flipper.flipInterval = 2000
        v_Flipper.isAutoStart = true

        // animation
        v_Flipper.setInAnimation(this,android.R.anim.slide_in_left)
        v_Flipper.setOutAnimation(this,android.R.anim.slide_out_right)

    }

}

