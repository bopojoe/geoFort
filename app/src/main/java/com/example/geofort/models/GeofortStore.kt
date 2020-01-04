package com.example.geofort.models

interface GeofortStore {
        fun findById(id:Long): GeofortModel?
        fun findAll(): List<GeofortModel>
        fun findAllByUser(userId: String): List<GeofortModel>
        fun create(geofort: GeofortModel)
        fun update(geofort: GeofortModel)
        fun delete(geofort: GeofortModel)
        fun clear()
    
}