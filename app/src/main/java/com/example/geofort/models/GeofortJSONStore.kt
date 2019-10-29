package com.example.geofort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import com.example.geofort.helpers.*
import java.util.*

val JSON_FILE = "geoforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<GeofortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GeofortJSONStore : GeofortStore, AnkoLogger {

    val context: Context
    var geoforts = mutableListOf<GeofortModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }

    }

    override fun findAll(): MutableList<GeofortModel> {
        return geoforts
    }

    override fun create(geofort: GeofortModel) {
        geofort.id = generateRandomId()
        geoforts.add(geofort)
        serialize()
    }


    override fun update(geofort: GeofortModel) {
        var foundGeofort: GeofortModel? = geoforts.find { p -> p.id == geofort.id }
        if (foundGeofort != null) {
            foundGeofort.title = geofort.title
            foundGeofort.description = geofort.description
            foundGeofort.image = geofort.image
            foundGeofort.lat = geofort.lat
            foundGeofort.lng = geofort.lng
            foundGeofort.zoom = geofort.zoom
            serialize()
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(geoforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        geoforts = Gson().fromJson(jsonString, listType)
    }
}