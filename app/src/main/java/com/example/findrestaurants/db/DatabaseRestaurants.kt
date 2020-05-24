package com.example.findrestaurants.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.findrestaurants.db.dao.RestaurantDao
import com.example.findrestaurants.db.models.Restaurant

@Database(entities = arrayOf(Restaurant::class), version = 1)
abstract class DatabaseRestaurants : RoomDatabase() {
    abstract fun getRestaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var instance: DatabaseRestaurants? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseRestaurants {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    DatabaseRestaurants::class.java,
                    "restaurants.db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as DatabaseRestaurants
        }
    }
}