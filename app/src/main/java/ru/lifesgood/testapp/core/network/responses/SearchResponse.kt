package ru.lifesgood.testapp.core.network.responses

import ru.lifesgood.testapp.core.network.data.City
import ru.lifesgood.testapp.core.network.data.Hotel

data class SearchResponse (
    var cities: List<City>? = null,
    var hotels: List<Hotel>? = null
)
