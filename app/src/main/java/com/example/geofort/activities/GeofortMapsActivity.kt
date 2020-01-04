package com.example.geofort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geofort.R
import com.example.geofort.helpers.readImageFromPath
import com.example.geofort.main.MainApp
import com.example.geofort.models.GeofortModel
import com.example.geofort.models.listType
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_geofort_map.*
import kotlinx.android.synthetic.main.card_geofort.view.*
import kotlinx.android.synthetic.main.content_geofort_map.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class GeofortMapsActivity: AnkoLogger, AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        setContentView(R.layout.activity_geofort_map)
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    fun configureMap() {
        map.setOnMarkerClickListener(this)
        map.uiSettings.setZoomControlsEnabled(true)
        info("James in here")
        var defaultGeoforts: ArrayList<GeofortModel>

        defaultGeoforts  = Gson().fromJson("[{\"date\":\"\",\"description\":\"Rathnabart\",\"id\":-6083374858026796000,\"image\":\"R.drawable.ballybroony\",\"imageList\":[\"R.drawable.ballybroony\"],\"lat\":53.5208297702936,\"lng\":-9.18883703649044,\"note\":\"Notes About the Geofort \\n Oval contour enclosure surrounding the summit of low-lying hilltop overlooking level terrain in all directions and the former lake known as Lough Dalla to the immediate NW.\",\"title\":\"Ballybroony\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Kinlough, Cahermore\",\"id\":-6083374858026796000,\"image\":\"R.drawable.brodullaghsouth\",\"imageList\":[\"R.drawable.brodullaghsouth\"],\"lat\":53.5208297702936,\"lng\":-9.18883703649044,\"note\":\"Notes About the Geofort \\n The outer enclosing element survives well for much of its circuit. The inner enclosing feature has been destroyed at the E, S and W.\",\"title\":\"Brodullagh South\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Northern Ireland Sites\",\"id\":-6083374858026796000,\"image\":\"R.drawable.cabraghfort\",\"imageList\":[\"R.drawable.cabraghfort\"],\"lat\":54.458091,\"lng\":-7.585222,\"note\":\"Notes About the Geofort \\n Circular contour fort on summit of hill with panoramic views from the summit.\",\"title\":\"Cabragh Fort\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Tievecrom Hill, Carrickastickan\",\"id\":-6083374858026796000,\"image\":\"R.drawable.forkill\",\"imageList\":[\"R.drawable.forkill\"],\"lat\":54.077165,\"lng\":-6.436602,\"note\":\"Notes About the Geofort \\n Univallate contour fort in commanding position on domed summit of Forkill Mountain with panoramic views from the summit and overlooking the modern town of Forkill to the immediate W.\",\"title\":\"Forkill\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Cloonyconry More\",\"id\":-6083374858026796000,\"image\":\"R.drawable.foymoylebeg\",\"imageList\":[\"R.drawable.foymoylebeg\"],\"lat\":52.791054,\"lng\":-8.587249,\"note\":\"Notes About the Geofort \\n Large Multiple enclosure surrounding the domed summit of hilltop. Positioned in commanding location at edge of upland terrain, overlooking the Broadford River and Glenomra River and an important mountain pass running E-W through\",\"title\":\"Foymoyle Beg\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30}]", listType
        )
        val newForts =  app.geoforts.findAll()
        defaultGeoforts.addAll(app.geoforts.findAll())
        defaultGeoforts.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.hiker))
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        currentTitle.text = marker.title
        val geofort = marker.tag as GeofortModel
        if (geofort != null) {
            currentDescription.text = geofort.description
                if(geofort.image.contains("http") ){
                    Picasso.get().load(geofort.image).into(currentImage);
                }else if (geofort.image.contains("content")){

                    currentImage.setImageBitmap(
                        readImageFromPath(
                            currentImage.context,
                            geofort.image
                        )
                    )

            }else if (geofort.image.contains("R.drawable")){
                var imagename = geofort.image
                info("james list view name $imagename")
                var result = when(geofort.image) {

                    "R.drawable.ballybroony" -> R.drawable.ballybroony
                    "R.drawable.brodullaghsouth" -> R.drawable.brodullaghsouth
                    "R.drawable.cabraghfort" -> R.drawable.cabraghfort
                    "R.drawable.forkill" -> R.drawable.forkill
                    "R.drawable.foymoylebeg" -> R.drawable.foymoylebeg
                    else -> 0
                }
                currentImage.setImageResource(result)
            }
            currentImage.setImageBitmap(readImageFromPath(currentImage.context, geofort.image))
        }

        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
