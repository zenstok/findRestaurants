package com.example.findrestaurants.services

import com.example.findrestaurants.placesAPI.models.MyPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearbyRestaurants(@Url url:String) : Call<MyPlaces>
}