package ru.lifesgood.testapp.ui.search.presenter

import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.lifesgood.testapp.R
import ru.lifesgood.testapp.core.app.bearing
import ru.lifesgood.testapp.ui.search.view.SearchView
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.disposables.Disposable
import ru.lifesgood.testapp.core.network.data.City
import ru.lifesgood.testapp.core.network.data.Location

class SearchPresenter(var view: SearchView? = null ): OnMapReadyCallback {

    private val compositeDisposable = CompositeDisposable()
    private var flightDisposable: Disposable? = null
    private lateinit var mMap: GoogleMap
    private var startPoint = LatLng(0.0, 0.0)
    private var endPoint = LatLng(0.0, 0.0)
    private lateinit var planeMarker: Marker
    private val pattern = arrayListOf(Dot(), Gap(25F))
    private val pathOptions= PolylineOptions()
        .width(10f)
        .color(Color.BLUE)
        .geodesic(false)
        .pattern(pattern)

    var departure: City? = null
    var arrival: City? = null

    fun onViewAttach(view: SearchView?){
        this.view = view
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let { mMap = it}
        initFlight()
    }

    private fun initFlight() {
        val pathChanged = pathChanged(departure?.location, arrival?.location)
        departure?.location?.toLatLng()?.let{
            this.startPoint = it
        }
        arrival?.location?.toLatLng()?.let{
            this.endPoint = it
        }

        if (pathChanged){
            mMap.clear()
            planeMarker = setPlaneMarker(mMap, endPoint)
            drawStartEndPoints(mMap, startPoint, endPoint)
            drawPath(startPoint, endPoint, mMap)
            startFlight()
        }else {
            planeMarker = setPlaneMarker(mMap, endPoint)
            drawStartEndPoints(mMap, startPoint, endPoint)
            mMap.addPolyline(pathOptions)
        }
    }

    private fun startFlight() {
        if (flightDisposable?.isDisposed == false)
            flightDisposable?.dispose()
        flightDisposable = Flowable.fromIterable(pathOptions.points)
                .delay (30, TimeUnit.MILLISECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ moveAirplane(planeMarker, it)}, {view?.showError(it)})
        compositeDisposable.add(flightDisposable!!)
    }

    private fun pathChanged(startPoint: Location?, endPoint: Location?): Boolean {
        return this.startPoint != startPoint?.toLatLng() || this.endPoint != endPoint?.toLatLng()
    }

    private fun moveAirplane(planeMarker: Marker?, point: LatLng) {
        planeMarker?.let {
            it.rotation = it.position.bearing(point).toFloat()
            it.position = point
        }
    }

    private fun drawPath(start: LatLng, end: LatLng, map: GoogleMap) {
        val pA = LatLng(start.latitude, end.longitude)
        val pB = LatLng(end.latitude, start.longitude)
        pathOptions.points.clear()
        pathOptions.points.addAll(calculateCubicBezier(start, pA, pB, end))
        map.addPolyline(pathOptions)
        moveToRouteBounds(map, start, end, pA, pB)
    }

    private fun drawStartEndPoints(map: GoogleMap, start: LatLng, end: LatLng) {
        map.addMarker(
            MarkerOptions()
                .position(start)
                .flat(true)
        )
        map.addMarker(
            MarkerOptions()
                .position(end)
                .flat(true)
        )
    }

    private fun setPlaneMarker(map: GoogleMap, point: LatLng): Marker {
        return map.addMarker( MarkerOptions()
            .position(point)
            .anchor(0.5f, 0.5f)
            .flat(true)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
        )
    }

    private fun moveToRouteBounds(googleMap: GoogleMap, vararg points: LatLng) {
        val builder = LatLngBounds.Builder()
        points.forEach {
            builder.include(it)
        }
        val bounds = builder.build()
        val padding = 120
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
    }

    private fun calculateCubicBezier(p1: LatLng, p2: LatLng, pA: LatLng, pB: LatLng): Collection<LatLng> {
        val pathPoints = arrayListOf<LatLng>()
        val pointsCount = 25000
        val step = 1.0 / pointsCount
        (1..pointsCount).forEach{
            pathPoints.add(calculatePoint(it*step, p1, p2, pA, pB))
        }
        return pathPoints
    }

    private fun calculatePoint(t: Double, p1: LatLng, p2: LatLng, pA: LatLng, pB: LatLng): LatLng {
        val r = 1 - t
        val arcX = r.pow(3) * p1.latitude + 3 * r.pow(2) * t * p2.latitude + 3 * (r) *
                t.pow(2) * pA.latitude + t.pow(3) * pB.latitude
        val arcY = (r * r * r) * p1.longitude + 3 * (r * r) * t * p2.longitude + 3 * (r) * t * t *
                pA.longitude + t * t * t * pB.longitude
        return LatLng(arcX, arcY)
    }

    fun onDestroy(){
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }
}
