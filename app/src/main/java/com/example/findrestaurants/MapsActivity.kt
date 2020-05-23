package com.example.findrestaurants

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.findrestaurants.placesAPI.Common
import com.example.findrestaurants.placesAPI.models.MyPlaces
import com.example.findrestaurants.services.IGoogleAPIService
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private val MY_PERMISSION_CODE: Int = 1000
    }

    lateinit var mService: IGoogleAPIService
    lateinit internal var currentPlace:MyPlaces

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //init service
        mService = Common.googleApiServices

        //Request runtime permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest();
                buildLocationCallBack();

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                );
            }
        } else {
            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            );
        }

    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0!!.locations.size - 1) //Get last location

                if (mMarker != null) {
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude, longitude)
                val marketOptions = MarkerOptions()
                    .position(latLng)
                    .title("Your position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mMarker = mMap!!.addMarker(marketOptions)

                //Move Camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
                nearByRestaurant()
            }
        }

    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermission())
                            mMap!!.isMyLocationEnabled = true

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun nearByRestaurant() {
        mMap.clear()
        val url = getUrl(latitude, longitude)

        mService.getNearbyRestaurants(url)
            .enqueue(object : Callback<MyPlaces> {
                override fun onResponse(call: Call<MyPlaces>?, response: Response<MyPlaces>?) {
                    currentPlace = response!!.body()!!
                    if(response!!.isSuccessful)
                    {
                        for(i in 0 until response!!.body()!!.results!!.size)
                        {
                            val markerOptions = MarkerOptions()
                            val googlePlace = response.body()!!.results!![i]
                            val lat = googlePlace.geometry!!.location!!.lat
                            val lng = googlePlace.geometry!!.location!!.lng
                            val placeName = googlePlace.name
                            val rating = googlePlace.rating
                            val latLng = LatLng(lat, lng)

                            markerOptions.snippet("Rating:" + rating)
                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            mMap!!.addMarker(markerOptions)
                        }
                    }
                }

                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    Toast.makeText(baseContext, ""+t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getUrl(latitude: Double, longitude: Double): String {
        val googlePlacesUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?radius=5000&type=restaurant&keyword=cruise&key=AIzaSyAn2FYdW0i5aJdW1gsrzdB5hmUZQN_HQO0")
        googlePlacesUrl.append("&location="+ latitude + "," + longitude)

        return googlePlacesUrl.toString()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            mMap!!.isMyLocationEnabled = true
        }

        mMap.uiSettings.isZoomControlsEnabled = true

    }
}
