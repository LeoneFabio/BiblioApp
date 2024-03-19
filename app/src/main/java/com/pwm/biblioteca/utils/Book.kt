package com.pwm.biblioteca.utils

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class Book(
    val copertina: Bitmap?,
    val id: Int,
    val titolo: String?,
    val autore: String?,
    val genere: String?,
    var numCopie: Int,
    var dataCaricamento: String?,
    var descrizione: String?,
    var giorniRestituzione:Int = -1
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(copertina, flags)
        parcel.writeInt(id)
        parcel.writeString(titolo)
        parcel.writeString(autore)
        parcel.writeString(genere)
        parcel.writeInt(numCopie)
        parcel.writeString(dataCaricamento)
        parcel.writeString(descrizione)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
