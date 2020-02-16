package ru.lifesgood.testapp.ui.inputAirports.repository

import io.reactivex.Single
import ru.lifesgood.testapp.core.network.responses.SearchResponse

interface InputAirportRepository {

    fun getAirports(term: String, language: String): Single<SearchResponse>

}
