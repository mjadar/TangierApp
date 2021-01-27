package com.example.tangierapplication.models

import android.text.TextUtils
import androidx.annotation.Keep
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*


data class Review(
        var rating:Float=0f,
        var userId:String="",
        var userName:String="",
        var textR:String="",
//       @ServerTimestamp var timestamp: FieldValue =FieldValue.serverTimestamp()
        @ServerTimestamp var timestamp: Date? = null

):Serializable{
    constructor( rating: Float,user:FirebaseUser, text: String):this() {
        this.userId = userId
        this.rating = rating
        this.textR = text
        this.userName = user.displayName.toString()
        if (TextUtils.isEmpty(this.userName)) {
            this.userName = user.email.toString()
        }
    }
}