package com.example.findrestaurants.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "restaurants")
data class Restaurant(@PrimaryKey(autoGenerate = true) var id: Long = 0) : Serializable {
    lateinit var name: String;
    lateinit var description: String;
    var rating: Double = 0.0;

    constructor(name: String, description: String, rating: Double) : this() {
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    override fun toString(): String {
        return "Restaurant(id=$id, name=$name, description=$description, rating=$rating";
    }
}