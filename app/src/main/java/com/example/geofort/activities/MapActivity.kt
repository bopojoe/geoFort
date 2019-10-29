package com.example.geofort.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.geofort.R
import com.example.geofort.models.Location

class MapActivity : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {


    private lateinit var map: GoogleMap
    var location = Location()

    var locationArr = arrayListOf(
        MapObjects("home",  LatLng(52.407211, -6.936461), false),
        MapObjects("walton",  LatLng(52.2457316,-7.1371349), false),
        MapObjects("ukraine??",  LatLng(49.575027, 34.627099), false),
        MapObjects("Pc World London",  LatLng(51.520771, -0.088520), false),
        MapObjects("limerick",  LatLng(52.634221, -8.650915), true)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        location = intent.extras?.getParcelable<Location>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//
//        map = googleMap
//
//
//        for ( obj in locationArr){
//            var loc = obj.pos
//            val options = MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hiker))
//                .title(obj.title)
//                .snippet("GPS : $loc")
//                .draggable(obj.dragable)
//                .position(loc)
//            map.addMarker(options)
//            map.setOnMarkerDragListener(this)
//        }
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.2364155,0.4010814), 5.29f))
//    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerDragListener(this)
        map.setOnMarkerClickListener(this)

        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Geofort")
            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }
    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : " + loc.toString()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(location.lat, location.lng)
        marker.setSnippet("GPS : " + loc.toString())
        return false
    }



    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}
