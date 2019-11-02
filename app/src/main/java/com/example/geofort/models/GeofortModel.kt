package com.example.geofort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList


@Parcelize
data class GeofortModel(
                        var userId: String = "",
                        var id: Long = 0,
                        var title: String = "",
                        var description: String = "",
                        var image: String = "",
                        var imageList: ArrayList<String> = ArrayList(),
                        var note: String = "",
                        var visited: Boolean = false,
                        var date: String = "",
                        var lat : Double = 0.0,
                        var lng: Double = 0.0,
                        var zoom: Float = 0f) : Parcelable
