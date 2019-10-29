package com.example.geofort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Location(var lat : Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
