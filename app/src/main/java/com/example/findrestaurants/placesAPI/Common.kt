package com.example.findrestaurants.placesAPI

import com.example.findrestaurants.services.IGoogleAPIService
import com.google.android.gms.maps.internal.IGoogleMapDelegate
import retrofit2.Retrofit

object Common {
    private val GOOGLE_API_URL="https://https://maps.googleapis.com/"

    val googleApiServices: IGoogleAPIService
    get() = RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    var radius = 5000
}