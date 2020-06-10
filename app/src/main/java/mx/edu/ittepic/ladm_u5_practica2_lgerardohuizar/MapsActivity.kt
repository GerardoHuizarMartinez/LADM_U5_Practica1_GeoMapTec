package mx.edu.ittepic.ladm_u5_practica2_lgerardohuizar

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var locacionCliente : FusedLocationProviderClient


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        locacionCliente = LocationServices.getFusedLocationProviderClient(this)

    }//End on create


    override fun onMapReady(googleMap: GoogleMap) {
        var extras = intent.extras!!
        var nameMark = extras.getString("name")
        var lati = extras.getDouble("latitude")
        var longi = extras.getDouble("longitude")

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.isMyLocationEnabled = true
        locacionCliente.lastLocation.addOnSuccessListener {
            if(it!=null){
                val posActual = LatLng(it.latitude, it.altitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posActual,10f))
            }
        }//End addOnSuccessListener

        val mark = LatLng(lati, longi)
        mMap.addMarker(MarkerOptions().position(mark).title(nameMark))

    }//End of MapReady


}//Enc claas Maps
