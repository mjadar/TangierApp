package com.example.tangierapplication.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Hotel(
        @DocumentId var hotelId: String? ="",
        var name: String? ="",
//        var photo: String? = null,
        var description: String? ="",
        var adresse: String? ="",
        var avgRating:Float=0f,
        var numRating:Int=0
) :Parcelable{
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readFloat(),
                parcel.readInt()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(hotelId)
                parcel.writeString(name)
                parcel.writeString(description)
                parcel.writeString(adresse)
                parcel.writeFloat(avgRating)
                parcel.writeInt(numRating)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Hotel> {
                override fun createFromParcel(parcel: Parcel): Hotel {
                        return Hotel(parcel)
                }

                override fun newArray(size: Int): Array<Hotel?> {
                        return arrayOfNulls(size)
                }
        }


}