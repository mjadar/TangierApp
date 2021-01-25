package com.example.tangierapplication.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue

data class Review(
        var rating:Float=0f,
        var userId:String="",
        var textR:String="",
        var timestamp: FieldValue =FieldValue.serverTimestamp()
){
}