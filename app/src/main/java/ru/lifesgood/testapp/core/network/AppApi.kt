package ru.lifesgood.testapp.core.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lifesgood.testapp.core.network.responses.SearchResponse

interface AppApi {

    @GET("/autocomplete?term=мос&lang=en")
    fun findAirports(
        @Query ("term")term: String,
        @Query("lang")language: String ): Single<SearchResponse>
}
