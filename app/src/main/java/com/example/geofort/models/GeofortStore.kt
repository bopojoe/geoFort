package com.example.geofort.models

interface GeofortStore {
        fun findAll(): List<GeofortModel>
        fun create(geofort: GeofortModel)
        fun update(geofort: GeofortModel)
    
}