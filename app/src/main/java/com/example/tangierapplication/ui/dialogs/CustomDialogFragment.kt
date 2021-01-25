package com.example.tangierapplication.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tangierapplication.R
import com.example.tangierapplication.models.Review
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_rating.*
import kotlinx.android.synthetic.main.dialog_rating.view.*
import java.lang.Exception

class CustomDialogFragment : DialogFragment(){
    private var ratingListener: RatingListener? = null

    internal interface RatingListener {
        fun onRating(rating: Review)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView:View = inflater.inflate(R.layout.dialog_rating,container,false)

        rootView.restaurantFormCancel.setOnClickListener {
            onCancelClicked()
        }

        rootView.restaurantFormButton.setOnClickListener {
           onSubmitClicked()
        }
        return rootView
    }

    private fun onSubmitClicked() {
        val user = Firebase.auth.currentUser
        user?.let {
            val rating = Review(
                    restaurantFormRating.rating,
                    it.uid,
                    restaurantFormText.text.toString())

//            ratingListener = targetFragment as RatingListener
            ratingListener?.onRating(rating)

        }

        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            ratingListener = targetFragment as RatingListener?
        } catch (e: Exception) {
            throw ClassCastException("Calling Fragment must implement OnAddFriendListener")
        }
    }

    private fun onCancelClicked() {
        dismiss()
    }
}