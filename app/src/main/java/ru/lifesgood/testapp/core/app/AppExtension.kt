package ru.lifesgood.testapp.core.app

import android.view.View
import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

fun LatLng.bearing(latLng: LatLng): Double {
    val latRad = Math.toRadians(this.latitude)
    val lngRad = Math.toRadians(this.longitude)
    val latRad1 = Math.toRadians(latLng.latitude)
    val lngRad1 = Math.toRadians(latLng.longitude)
    val lngDiff = lngRad1 - lngRad
    val y = sin(lngDiff) * cos(latRad1)
    val x = cos(latRad) * sin(latRad1) - sin(latRad) * cos(latRad1) * cos(lngDiff)
    var bearing = atan2(y, x)
    bearing = Math.toDegrees(bearing)
    bearing = (bearing + 360) % 360
    return bearing
}

fun View.setVisibility(visibility: Boolean, invisibleType: Int = View.GONE){
    this.visibility = when(visibility){
        true -> View.VISIBLE
        else -> invisibleType
    }
}