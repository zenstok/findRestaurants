package com.example.findrestaurants.db.providers

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.findrestaurants.db.repositories.RestaurantRepository

class DatabaseProvider: ContentProvider() {
    private lateinit var restaurantRepository: RestaurantRepository

    override fun onCreate(): Boolean {
        restaurantRepository = RestaurantRepository(null, context)
        return true;
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        Log.d("CONTENT PROVIDER", restaurantRepository.selectlAllRestaurants().get(0).name)
        return p0
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return null
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun getType(p0: Uri): String? {
        return null
    }
}