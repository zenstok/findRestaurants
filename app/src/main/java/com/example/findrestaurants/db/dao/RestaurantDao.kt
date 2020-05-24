package com.example.findrestaurants.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.findrestaurants.db.models.Restaurant

@Dao
abstract class RestaurantDao {
    @Insert
    abstract fun insertRestaurant(restaurant: Restaurant): Long

    @Transaction
    @Query("SELECT * FROM restaurants")
    abstract fun selectAllRestaurants(): List<Restaurant>

    @Transaction
    @Query("SELECT * from restaurants where rating > 4")
    abstract fun selectRestaurantsWithGoodRating(): List<Restaurant>

    @Transaction
    @Query("DELETE FROM restaurants")
    abstract fun deleteAllRestaurants()

    @Transaction
    @Query("UPDATE restaurants set name = :name, description = :description, rating = :rating where id= :id")
    abstract fun updateRestaurant(id: Long, name: String, description: String, rating: Double)
}
