package com.example.findrestaurants

import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.example.findrestaurants.db.DatabaseRestaurants
import com.example.findrestaurants.db.models.Restaurant
import com.example.findrestaurants.db.providers.DatabaseProvider
import com.example.findrestaurants.db.repositories.RestaurantRepository

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var restaurantRepository: RestaurantRepository

    companion object {
        private val MY_PERMISSION_CODE: Int = 1000
        private var LOCATION_REQUEST_GRANTED = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_map, R.id.nav_map_settings
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
            Restaurant("Restaurant1", "Restaurant with best description", 4.0
            )
        )
        restaurantRepository.insertRestaurant(
            Restaurant("Restaurant2", "Restaurant with best description 2", 3.0
            )
        )
    }

    private fun getRestaurants() {
        val restaurants = restaurantRepository.selectlAllRestaurants()
        restaurants.get(0).name = "Updated restaurant name"
        restaurantRepository.updateRestaurant(restaurants.get(0))
        Log.d("First restaurant in our db", restaurants.get(0).name)
//        var dp = DatabaseProvider().
//        Log.d("BANANA", dp.insert(Uri.parse("http://www.google.com"), ContentValues()).toString())
    }

}
