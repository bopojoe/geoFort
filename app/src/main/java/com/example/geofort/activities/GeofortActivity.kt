package com.example.geofort.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_geofort.*
import kotlinx.android.synthetic.main.activity_geofort.description
import kotlinx.android.synthetic.main.activity_geofort.geofortTitle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import com.example.geofort.R
import com.example.geofort.helpers.readImageFromPath
import com.example.geofort.helpers.showImagePicker
import com.example.geofort.main.MainApp
import com.example.geofort.models.Location
import com.example.geofort.models.GeofortModel

class GeofortActivity : AppCompatActivity(), AnkoLogger {

    var geofort = GeofortModel()
    val IMAGE_REQUEST = 1

    val LOCATION_REQUEST = 2
    var location = Location(52.245696, -7.139102, 30f)

    lateinit var app: MainApp

    var edit = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofort)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Geofort Activity started..")

        app = application as MainApp

        if (intent.hasExtra("geofort_edit")) {
            edit = true
            geofort = intent.extras?.getParcelable<GeofortModel>("geofort_edit")!!
            geofortTitle.setText(geofort.title)
            description.setText(geofort.description)
            chooseImage.setText(R.string.button_changeImage)
            geofortLocation.setText(R.string.change_location)
            location = Location(geofort.lat, geofort.lng, geofort.zoom)
            info("location $location")
            geofortImageList.setImageBitmap(readImageFromPath(this, geofort.image))
        }

        geofortLocation.setOnClickListener {
            info("location $location")
            startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
        }



        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(edit){
            menuInflater.inflate(R.menu.menu_geofort_edit, menu)
        }else{
        menuInflater.inflate(R.menu.menu_geofort, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.delete -> {
                app.geoforts.delete(geofort)
                finish()
                toast("deleted")
            }
            R.id.Add -> {
                geofort.userId = app.currentuser
                geofort.title = geofortTitle.text.toString()
                geofort.description = description.text.toString()
                geofort.lng = location.lng
                geofort.lat = location.lat
                geofort.zoom = location.zoom
                if (geofort.title.isNotEmpty()) {
                    if(edit){
                        app.geoforts.update(geofort)
                        info("save Button Pressed: $geofort")
                    }else{
                        app.geoforts.create(geofort.copy())
                        info("add Button Pressed: $geofort")
                    }
                    setResult(AppCompatActivity.RESULT_OK)
                    finish()
                } else {
                    toast(R.string.add_emptyGeofort)
                }
            }
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    geofort.image = data.getData().toString()
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    location = data.extras?.getParcelable<Location>("location")!!
                }
            }
        }
    }
}