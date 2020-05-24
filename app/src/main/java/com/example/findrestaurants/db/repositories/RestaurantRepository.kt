package com.example.findrestaurants.db.repositories

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.findrestaurants.db.DatabaseRestaurants
import com.example.findrestaurants.db.dao.RestaurantDao
import com.example.findrestaurants.db.models.Restaurant

class RestaurantRepository(application: Application?, context: Context? = null) {
    private val db: DatabaseRestaurants
    private val restaurantDao: RestaurantDao


    init {
        if(context == null) {
            db = DatabaseRestaurants.getInstance(application!!.applicationContext)
        } else {
            db = DatabaseRestaurants.getInstance(context)
        }
        restaurantDao = db.getRestaurantDao()
    }

    fun selectlAllRestaurants(): List<Restaurant> {
        return restaurantDao.selectAllRestaurants()
    }

    fun selectGoodRatingRestaurants(): List<Restaurant> {
        return restaurantDao.selectRestaurantsWithGoodRating()
    }

    fun insertRestaurant(restaurant: Restaurant) {
        restaurantDao.insertRestaurant(restaurant)
    }

    fun updateRestaurant(restaurant: Restaurant) {
        restaurantDao.updateRestaurant(restaurant.id, restaurant.name, restaurant.description, restaurant.rating)
    }

    fun deleteAllRestaurants() {
        restaurantDao.deleteAllRestaurants()
    }
}