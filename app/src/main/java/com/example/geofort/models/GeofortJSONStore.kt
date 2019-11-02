package com.example.geofort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import com.example.geofort.helpers.*
import kotlinx.android.synthetic.main.activity_geofort.*
import org.jetbrains.anko.info
import java.util.*

val JSON_FILE = "geoforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<GeofortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GeofortJSONStore : GeofortStore, AnkoLogger {
    override fun findAllByUser(userId: String): ArrayList<GeofortModel> {
        var returnlist = ArrayList<GeofortModel>()
        for (geofort in geoforts){
            if(userId == geofort.userId){
                returnlist.add(geofort)
            }
        }
        return returnlist
    }

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
            
            foundGeofort.imageList = geofort.imageList
            foundGeofort.note = geofort.note
            foundGeofort.date = geofort.date
            foundGeofort.visited = geofort.visited
            foundGeofort.lat = geofort.lat
            foundGeofort.lng = geofort.lng
            foundGeofort.zoom = geofort.zoom
            serialize()
        }
    }

    override fun delete(geofort: GeofortModel) {
            geoforts.remove(geofort)
            serialize()
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