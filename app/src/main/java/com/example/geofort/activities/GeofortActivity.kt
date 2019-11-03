package com.example.geofort.activities


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.get
import androidx.core.view.size


class GeofortActivity : AppCompatActivity(), AnkoLogger {

    var geofort = GeofortModel()
    var imageList = ArrayList<String>()
    var numOfImage = 0
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
            info("james "+geofort.imageList)
            edit = true
            geofort = intent.extras?.getParcelable<GeofortModel>("geofort_edit")!!
            geofortTitle.setText(geofort.title)
            description.setText(geofort.description)
            visited.isChecked = geofort.visited
            date.setText(geofort.date)
            chooseImage.setText(R.string.button_changeImage)
            geofortLocation.setText(R.string.change_location)
            location = Location(geofort.lat, geofort.lng, geofort.zoom)
            note.text = geofort.note
            imageList = geofort.imageList
            for(image in imageList){
                val imageView = ImageView(this)
                if (geofort.userId != "defaultLocations"){
                imageView.setImageBitmap(readImageFromPath(this, image))
                }else{
                    info("james imagename $image")
                    var result = when(image) {
                        "R.drawable.ballybroony" -> R.drawable.ballybroony
                        "R.drawable.brodullaghsouth" -> R.drawable.brodullaghsouth
                        "R.drawable.cabraghfort" -> R.drawable.cabraghfort
                        "R.drawable.forkill" -> R.drawable.forkill
                        "R.drawable.foymoylebeg" -> R.drawable.foymoylebeg
                       else -> 0
                    }
                    info("result was $result")
                    imageView.setImageResource(result)
                }
                val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(100,20,100,20)
                imageView.layoutParams = layoutParams
                val layout = findViewById<LinearLayout>(R.id.image_holder)
                layout.addView(imageView)
            }
            info("location $location")
//            geofortImageList.setImageBitmap(readImageFromPath(this, geofort.image))
        }

        geofortLocation.setOnClickListener {
            info("location $location")
            startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)

        }

        btn_addNote.setOnClickListener {
            val textView = findViewById<TextView>(R.id.note)
            val writing = textView.text
            val newText = editNote.text.toString()
            textView.text = "$writing \n $newText"
            editNote.text = null

        }
        visited.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDate = sdf.format(Date())
            date.setText(currentDate)
            }else{
                date.setText("")
            }



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
                info("james add"+app.imageList )
                app.imageList.addAll(geofort.imageList)
                geofort.userId = app.currentuser
                geofort.imageList = app.imageList
                geofort.title = geofortTitle.text.toString()
                geofort.description = description.text.toString()
                geofort.note = note.text.toString()
                geofort.visited = visited.isChecked
                geofort.date = date.text.toString()
                geofort.lng = location.lng
                geofort.lat = location.lat
                geofort.zoom = location.zoom
                info("james add"+geofort.imageList )
                if (geofort.title.isNotEmpty()) {
                    if(edit){
                        app.geoforts.update(geofort)
                        info("save Button Pressed: $geofort")
                    }else{
                        app.geoforts.create(geofort.copy())
                        info("add Button Pressed: $geofort")
                    }
                    setResult(AppCompatActivity.RESULT_OK)
                    app.imageList = ArrayList()
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
                    val imageHolder = findViewById<LinearLayout>(R.id.image_holder)

                    val imageString = data.getData().toString()
                    val imageView = ImageView(this)
                    imageView.setImageBitmap(readImageFromPath(this, imageString))
                    val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    layoutParams.setMargins(100,20,100,20)
                    imageView.layoutParams = layoutParams
                    var imageList = app.imageList
                    imageList.add(data.getData().toString())
                    imageHolder.addView(imageView,numOfImage)
                    numOfImage += 1

                    geofort.image = data.getData().toString()
                    app.imageList = imageList

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