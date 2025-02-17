package com.example.nativeshop.Model

import android.os.Parcel
import android.os.Parcelable

// Parcelable is a way to define class that should pass between activities.
data class ItemModel(
    val title: String = "",
    val description: String = "",
    val picUrl: ArrayList<String> = ArrayList(),
    val model: ArrayList<String> = ArrayList(),
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val numberInCart: Int = 0,
    val showRecommended: Boolean = false,
    val categoryId: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeStringList(picUrl)
        parcel.writeStringList(model)
        parcel.writeDouble(price)
        parcel.writeDouble(rating)
        parcel.writeInt(numberInCart)
        parcel.writeByte(if (showRecommended) 1 else 0)
        parcel.writeString(categoryId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemModel> {
        override fun createFromParcel(parcel: Parcel): ItemModel {
            return ItemModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemModel?> {
            return arrayOfNulls(size)
        }
    }
}
