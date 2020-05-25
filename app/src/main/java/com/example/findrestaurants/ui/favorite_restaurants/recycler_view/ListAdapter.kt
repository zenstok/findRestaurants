package com.example.findrestaurants.ui.favorite_restaurants.recycler_view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.findrestaurants.R
import com.example.findrestaurants.db.models.Restaurant

class ListAdapter(private val list: List<Restaurant>)
    : RecyclerView.Adapter<RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RestaurantViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant: Restaurant = list[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int = list.size

}

class RestaurantViewHolder(inflater: LayoutInflater, parent: ViewGroup) :

    RecyclerView.ViewHolder(inflater.inflate(R.layout.favorite_restaurant, parent, false)) {
    private var mTitleView: TextView? = null
    private var mRatingView: TextView? = null


    init {
        mTitleView = itemView.findViewById(R.id.list_item_title)
        mRatingView = itemView.findViewById(R.id.list_item_rating)
    }

    fun bind(restaurant: Restaurant) {
        mTitleView?.text = restaurant.name
        mRatingView?.text = restaurant.rating.toString()
    }



}