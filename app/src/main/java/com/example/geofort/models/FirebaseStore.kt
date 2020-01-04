package com.example.geofort.models
import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import com.example.geofort.helpers.readImageFromPath
import com.example.geofort.models.GeofortModel
import com.example.geofort.models.GeofortStore
import java.io.ByteArrayOutputStream
import java.io.File

class FirebaseStore(val context: Context) : GeofortStore, AnkoLogger {

    val geoforts = ArrayList<GeofortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<GeofortModel> {
        return geoforts
    }

    override fun findAllByUser(userId: String): java.util.ArrayList<GeofortModel> {
        var returnlist = java.util.ArrayList<GeofortModel>()
        for (geofort in geoforts){
            if(userId == geofort.userId){
                returnlist.add(geofort)
            }
        }
        return returnlist
    }

    override fun findById(id: Long): GeofortModel? {
        return geoforts.find { p -> p.id == id }
    }

    override fun create(geofort: GeofortModel) {
        val key = db.child("users").child(userId).child("geoforts").push().key
        key?.let {
            geofort.fbId = key
            geoforts.add(geofort)
            db.child("users").child(userId).child("geoforts").child(key).setValue(geofort)
            updateImage(geofort)
        }
    }

    override fun update(geofort: GeofortModel) {
        var foundGeofort: GeofortModel? = geoforts.find { p -> p.fbId == geofort.fbId }
        if (foundGeofort != null) {
            foundGeofort.userId = geofort.userId
            foundGeofort.title = geofort.title
            foundGeofort.description = geofort.description
            foundGeofort.image = geofort.image
            foundGeofort.visited = geofort.visited
            foundGeofort.date = geofort.date
            foundGeofort.imageList = geofort.imageList
            foundGeofort.lat = geofort.lat
            foundGeofort.lng = geofort.lng
            foundGeofort.zoom = geofort.zoom

        }

        db.child("users").child(userId).child("geoforts").child(geofort.fbId)
            .setValue(geofort)
        if (geofort.image.length > 0 && geofort.image[0] != 'h') {
            updateImage(geofort)
        }
    }

    override fun delete(geofort: GeofortModel) {
        db.child("users").child(userId).child("geoforts").child(geofort.fbId).removeValue()
        geoforts.remove(geofort)
    }

    override fun clear() {
        geoforts.clear()
    }

    fun updateImage(geofort: GeofortModel) {
        if (geofort.image != "") {
            val fileName = File(geofort.image)
            val imageName = fileName.getName()

            var imageRef = st.child("$userId/$imageName")
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, geofort.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        geofort.image = it.toString()
                        db.child("users").child(userId).child("geoforts").child(geofort.fbId)
                            .setValue(geofort)
                    }
                }
            }
        }
    }

    fun fetchGeoforts(geofortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(geoforts) { it.getValue(GeofortModel::class.java) }
                geofortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        geoforts.clear()
        db.child("users").child(userId).child("geoforts")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}