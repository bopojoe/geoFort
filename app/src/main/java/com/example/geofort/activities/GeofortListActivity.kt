package com.example.geofort.activities
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_geofort_list.*
import com.example.geofort.R
import com.example.geofort.helpers.read
import com.example.geofort.login.LoginActivity
import com.example.geofort.main.MainApp
import com.example.geofort.models.GeofortModel
import com.example.geofort.models.JSON_FILE
import com.example.geofort.models.listType
import com.example.geofort.settings.UserSettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.json.JSONObject

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
            R.id.item_map -> {info("james item_map"); startActivityForResult<GeofortMapsActivity>(0);}
            R.id.item_add -> startActivityForResult<GeofortActivity>(0)

            R.id.logout -> {auth.signOut(); app.currentuser = "";  val intentLogin= Intent(this@GeofortListActivity, LoginActivity::class.java)

                startActivity(intentLogin)}

            R.id.user_settings -> startActivityForResult<UserSettingsActivity>(0)//{
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
        var defaultGeoforts: ArrayList<GeofortModel>
        defaultGeoforts = Gson().fromJson("[{\"date\":\"\",\"description\":\"Rathnabart\",\"id\":-6083374858026796000,\"image\":\"R.drawable.ballybroony\",\"imageList\":[\"R.drawable.ballybroony\"],\"lat\":53.5208297702936,\"lng\":-9.18883703649044,\"note\":\"Notes About the Geofort \\n Oval contour enclosure surrounding the summit of low-lying hilltop overlooking level terrain in all directions and the former lake known as Lough Dalla to the immediate NW.\",\"title\":\"Ballybroony\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Kinlough, Cahermore\",\"id\":-6083374858026796000,\"image\":\"R.drawable.brodullaghsouth\",\"imageList\":[\"R.drawable.brodullaghsouth\"],\"lat\":53.5208297702936,\"lng\":-9.18883703649044,\"note\":\"Notes About the Geofort \\n The outer enclosing element survives well for much of its circuit. The inner enclosing feature has been destroyed at the E, S and W.\",\"title\":\"Brodullagh South\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Northern Ireland Sites\",\"id\":-6083374858026796000,\"image\":\"R.drawable.cabraghfort\",\"imageList\":[\"R.drawable.cabraghfort\"],\"lat\":54.458091,\"lng\":-7.585222,\"note\":\"Notes About the Geofort \\n Circular contour fort on summit of hill with panoramic views from the summit.\",\"title\":\"Cabragh Fort\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Tievecrom Hill, Carrickastickan\",\"id\":-6083374858026796000,\"image\":\"R.drawable.forkill\",\"imageList\":[\"R.drawable.forkill\"],\"lat\":54.077165,\"lng\":-6.436602,\"note\":\"Notes About the Geofort \\n Univallate contour fort in commanding position on domed summit of Forkill Mountain with panoramic views from the summit and overlooking the modern town of Forkill to the immediate W.\",\"title\":\"Forkill\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30},{\"date\":\"\",\"description\":\"Cloonyconry More\",\"id\":-6083374858026796000,\"image\":\"R.drawable.foymoylebeg\",\"imageList\":[\"R.drawable.foymoylebeg\"],\"lat\":52.791054,\"lng\":-8.587249,\"note\":\"Notes About the Geofort \\n Large Multiple enclosure surrounding the domed summit of hilltop. Positioned in commanding location at edge of upland terrain, overlooking the Broadford River and Glenomra River and an important mountain pass running E-W through\",\"title\":\"Foymoyle Beg\",\"userId\":\"defaultLocations\",\"visited\":false,\"zoom\":30}]", listType)

        defaultGeoforts.addAll(app.geoforts.findAllByUser(app.currentuser))

        showGeoforts(defaultGeoforts)
    }

    fun showGeoforts (geoforts: List<GeofortModel>) {
        recyclerView.adapter = GeofortAdapter(geoforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


}