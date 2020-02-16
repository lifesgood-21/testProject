package ru.lifesgood.testapp.ui.main.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import ru.lifesgood.testapp.R
import ru.lifesgood.testapp.core.network.data.City
import ru.lifesgood.testapp.ui.main.repository.MainRemoteRepository
import ru.lifesgood.testapp.ui.main.view.MainView

class MainPresenter(
    var view: MainView?,
    val repository: MainRemoteRepository,
    private val applicationContext: Context
) {
    private var departure: City? = null
    private var arrival: City? = null

    fun onViewCreated(view: MainView){
        this.view = view
        displayArrivalAndDeparture()
    }

    private fun displayArrivalAndDeparture(){
        view?.displayArrival(arrival)
        view?.displayDeparture(departure)
    }

    fun handleDepartureInput(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            departure = data?.getSerializableExtra(City.SERIALIZABLE_NAME) as City
            departure?.let{view?.displayDeparture(it)}
        }
    }

    fun handleArrivalInput(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            arrival = data?.getSerializableExtra(City.SERIALIZABLE_NAME) as City
            arrival?.let{view?.displayArrival(it)}
        }
    }

    fun changeArrivalVsDeparture() {
        val arrival = this.arrival
        this.arrival = departure
        departure = arrival
        displayArrivalAndDeparture()
    }

    fun startSearch() {
        if (departure != null && arrival != null){
            if(departure == arrival){
                val text = applicationContext.getString(R.string.departure_must_be_different)
                val throwable = Throwable(text)
                view?.displayError(throwable)
            }
            view?.startSearch(departure!!, arrival!!)
        }else{
            val throwable = Throwable(applicationContext.getString(R.string.arrival_departure_error))
            view?.displayError(throwable)
        }
    }
}