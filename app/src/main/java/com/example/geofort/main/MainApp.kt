package com.example.geofort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.example.geofort.R
import com.example.geofort.models.*
import com.example.geofort.models.FirebaseStore
import com.google.firebase.auth.FirebaseUser
import java.lang.Thread.sleep

 class MainApp : Application(), AnkoLogger {


     var imageList = ArrayList<String>()
     var currentuser: String = ""
     var username: String = ""


    //val placemarks = ArrayList<PlacemarkModel>()
    //val placemarks = PlacemarkMemStore()
    lateinit var geoforts: GeofortStore

    override fun onCreate() {
        sleep(2000)

        setTheme(R.style.AppTheme)
        super.onCreate()
        geoforts = FirebaseStore(applicationContext)
        info("Geofort started")
    }
}