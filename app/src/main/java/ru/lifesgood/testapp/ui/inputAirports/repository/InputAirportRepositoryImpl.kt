package ru.lifesgood.testapp.ui.inputAirports.repository

import ru.lifesgood.testapp.core.network.AppApi

class InputAirportRepositoryImpl(private val api: AppApi): InputAirportRepository {

    override fun getAirports(term: String, language: String) = api.findAirports(term, language)

}