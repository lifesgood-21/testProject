package ru.lifesgood.testapp.ui.inputAirports.view

import ru.lifesgood.testapp.core.network.data.City

interface InputAirportView {

    fun airportSelected(city: City)
    fun displayErr(throwable: Throwable)
    fun displayEmptyListHint(visibility: Boolean)
    fun displayProgress(visibility: Boolean)
}
