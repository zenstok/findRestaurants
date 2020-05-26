package com.example.findrestaurants

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.UserDictionary
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.loader.content.CursorLoader
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.findrestaurants.db.models.Restaurant
import com.example.findrestaurants.db.repositories.RestaurantRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    public lateinit var restaurantRepository: RestaurantRepository

    companion object {
        private val MY_PERMISSION_CODE: Int = 1000
        private var LOCATION_REQUEST_GRANTED = false
        public lateinit var contentResolverVar: ContentResolver
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentResolverVar = contentResolver
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            goToMapActivity2()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_favorite_restaurants, R.id.nav_tutorial, R.id.nav_about_us, R.id.nav_map, R.id.nav_map_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        dbOperations()
        checkLocationPermission()
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
                    ), MainActivity.MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MainActivity.MY_PERMISSION_CODE
                )
            return false
        } else
            LOCATION_REQUEST_GRANTED = true
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun goToMapActivity(item: MenuItem) {
        if (LOCATION_REQUEST_GRANTED) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            return
        }
        checkLocationPermission()
    }

    fun goToMapActivity2() {
        if (LOCATION_REQUEST_GRANTED) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            return
        }
        checkLocationPermission()
    }

    fun goToMapSettingsActivity(item: MenuItem) {
        val intent = Intent(this, MapSettings::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_CODE) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                LOCATION_REQUEST_GRANTED = true
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun dbOperations() {
        restaurantRepository = RestaurantRepository(application)
        restaurantRepository.deleteAllRestaurants()
        insertRestaurants()
        getRestaurants()
    }
    private fun insertRestaurants() {
        restaurantRepository.insertRestaurant(
            Restaurant("Restaurant Excellence", "Restaurant with excellent description", 4.3
            )
        )
        restaurantRepository.insertRestaurant(
            Restaurant("Frantzén", "Restaurant with michelin stars", 5.0
            )
        )
    }

    private fun getRestaurants() {
        val restaurants = restaurantRepository.selectlAllRestaurants()
        restaurants.get(0).name = "Updated Restaurant Excellence"
        restaurantRepository.updateRestaurant(restaurants.get(0))
    }

}
