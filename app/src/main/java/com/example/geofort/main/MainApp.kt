package com.example.geofort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.example.geofort.R
import com.example.geofort.models.GeofortJSONStore
import com.example.geofort.models.GeofortMemStore
import com.example.geofort.models.GeofortModel
import com.example.geofort.models.GeofortStore
import java.lang.Thread.sleep

class MainApp : Application(), AnkoLogger {

    //val placemarks = ArrayList<PlacemarkModel>()
    //val placemarks = PlacemarkMemStore()
    lateinit var geoforts: GeofortStore

    override fun onCreate() {
        sleep(2000)

        setTheme(R.style.AppTheme)
        super.onCreate()
        geoforts = GeofortJSONStore(applicationContext)
        info("Geofort started")
    }
}