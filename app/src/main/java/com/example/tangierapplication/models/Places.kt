package com.example.tangierapplication.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Places(
        @DocumentId var placeId: String? ="",
        var name: String? ="",
        var pictures: MutableMap<String, String> = mutableMapOf(),
        var description: String? ="",
        var adresse: String? ="",
        var avgRating:Float=0f,
        var numRating:Int=0
) :Parcelable {
}