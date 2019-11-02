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
import com.example.geofort.login.LoginActivity
import com.example.geofort.main.MainApp
import com.example.geofort.models.GeofortModel
import com.example.geofort.settings.UserSettingsActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger

class GeofortListActivity : AppCompatActivity(), GeofortListener, AnkoLogger {

    lateinit var app: MainApp
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofort_list)
        app = application as MainApp
        auth = FirebaseAuth.getInstance()

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

            R.id.logout -> {auth.signOut(); app.currentuser = "";  val intentLogin= Intent(this@GeofortListActivity, LoginActivity::class.java)

                startActivity(intentLogin)}

            R.id.userSettings -> startActivityForResult<UserSettingsActivity>(0)//{
               //val intentSettings = Intent(this@GeofortListActivity, UserSettingsActivity::class.java)
               //startActivity(intentSettings)
           // }
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
        showGeoforts(app.geoforts.findAllByUser(app.currentuser))
    }

    fun showGeoforts (geoforts: List<GeofortModel>) {
        recyclerView.adapter = GeofortAdapter(geoforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


}