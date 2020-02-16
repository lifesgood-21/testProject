package ru.lifesgood.testapp.core.network.data

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Location(
    var lat: Float? = null,
    var lon: Float? = null ): Serializable {

    fun toLatLng() = LatLng(lat?.toDouble()?:0.0, lon?.toDouble()?:0.0)
}