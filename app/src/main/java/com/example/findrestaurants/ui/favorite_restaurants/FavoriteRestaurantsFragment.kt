package com.example.findrestaurants.ui.favorite_restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findrestaurants.MainActivity
import com.example.findrestaurants.R
import com.example.findrestaurants.ui.favorite_restaurants.recycler_view.ListAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class FavoriteRestaurantsFragment : Fragment() {

    private lateinit var favoriteRestaurantsViewModel: FavoriteRestaurantsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteRestaurantsViewModel =
            ViewModelProviders.of(this).get(FavoriteRestaurantsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        val mainActivity: MainActivity = activity as MainActivity

        recycler_view_fav_restaurants.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ListAdapter(mainActivity.restaurantRepository.selectlAllRestaurants())
        }
    }


}
