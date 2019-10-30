package com.example.geofort.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GeofortMemStore : GeofortStore, AnkoLogger {

    val geoforts = ArrayList<GeofortModel>()

    override fun findAll(): List<GeofortModel> {
        return geoforts
    }

    override fun create(geofort: GeofortModel) {
        geofort.id = getId()
        geoforts.add(geofort)
        logAll()
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
            logAll()
        }
    }

    override fun delete(geofort: GeofortModel) {
        geoforts.remove(geofort)
    }

    fun logAll() {
        geoforts.forEach { info("${it}") }
    }
}