package com.pwm.biblioteca.utils

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class News (
    val id: Int,
    val image: Bitmap?,
    val descrizione: String?,
    val dataPubblicazione: String?,
    val titolo: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeParcelable(image, flags)
        parcel.writeString(descrizione)
        parcel.writeString(dataPubblicazione)
        parcel.writeString(titolo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<News> {
        override fun createFromParcel(parcel: Parcel): News {
            return News(parcel)
        }

        override fun newArray(size: Int): Array<News?> {
            return arrayOfNulls(size)
        }
    }
}
