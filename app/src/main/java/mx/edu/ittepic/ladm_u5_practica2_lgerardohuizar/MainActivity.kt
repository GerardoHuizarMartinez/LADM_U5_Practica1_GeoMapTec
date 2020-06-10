package mx.edu.ittepic.ladm_u5_practica2_lgerardohuizar

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var remoteDB = FirebaseFirestore.getInstance()

    var arrayPlaces = ArrayList<Data>()
    var markers = ArrayList<String>()
    lateinit var location : LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //-------------------------- PERMISOS ----------------------------------------------------
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }//Fin del if para conceder permisos

        remoteDB.collection("tecnologico").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if(firebaseFirestoreException != null){
                textView.setText("Error: " + firebaseFirestoreException.message)
                return@addSnapshotListener
            }

            arrayPlaces = ArrayList<Data>()
            markers = ArrayList<String>()


            for(document in querySnapshot!!){
                var data = Data()
                data.name = document.getString("nombre").toString()
                data.position1 = document.getGeoPoint("posicion1")!!
                data.position2 = document.getGeoPoint("posicion2")!!


                arrayPlaces.add(data)
                markers.add(document.getString("nombre").toString())
            }

            var adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,markers)
           listViewPlacesTec.adapter = adapter

        }//End on Snapshoot



        listViewPlacesTec.setOnItemClickListener { adapterView, view, position, id ->
            var windMainActivity : Intent = Intent(this, MapsActivity::class.java)

            var name = markers[position]
            var latitud = arrayPlaces[position].position1.latitude
            var longitud = arrayPlaces[position].position2.longitude

            windMainActivity.putExtra("name",name)
            windMainActivity.putExtra("latitude",latitud)
            windMainActivity.putExtra("longitude",longitud)

            startActivity(windMainActivity)

        }//End on item Click Listener

        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var oyente = Oyente(this)
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,01f,oyente)

    }//End On Create

}//End MainActivity

class Oyente(puntero:MainActivity) : android.location.LocationListener{
    var p = puntero
    override fun onLocationChanged(location: Location) {
        var geoPosition = GeoPoint(location.latitude,location.longitude)
        for(item in p.arrayPlaces){
            if(item.imHere(geoPosition)){
                p.textView.setText("Tu ubicacion actual es: "+item.name)
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

}

