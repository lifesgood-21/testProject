package ru.lifesgood.testapp.ui.main.view

import ru.lifesgood.testapp.core.network.data.City

interface MainView {

    fun displayError(throwable: Throwable)

    fun displayArrival(arrival: City?)

    fun displayDeparture(departure: City?)

    fun startSearch(departure: City, arrival: City)
}