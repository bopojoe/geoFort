package com.example.geofort.activities
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_geofort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import com.example.geofort.R
import com.example.geofort.main.MainApp
import com.example.geofort.models.GeofortModel

class GeofortListActivity : AppCompatActivity(), GeofortListener {

    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofort_list)
        app = application as MainApp

        toolbar.title = title
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadGeoforts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<GeofortActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGeofortClick(geofort: GeofortModel) {
        startActivityForResult(intentFor<GeofortActivity>().putExtra("geofort_edit", geofort), 0)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        loadGeoforts()
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun loadGeoforts() {
        showGeoforts(app.geoforts.findAll())
    }

    fun showGeoforts (geoforts: List<GeofortModel>) {
        recyclerView.adapter = GeofortAdapter(geoforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


}