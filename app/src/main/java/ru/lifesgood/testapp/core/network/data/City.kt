package ru.lifesgood.testapp.core.network.data

import ru.lifesgood.testapp.core.view.BaseItemType
import ru.lifesgood.testapp.core.view.ListBaseItem
import java.io.Serializable

data class City(
    var countryCode: String? = null,
    var country: String? = null,
    var latinFullName: String? = null,
    var fullname: String? = null,
    var clar: String? = null,
    var latinClar: String? = null,
    var location: Location? = null,
    var hotelsCount: Int? = null,
    var iata: List<String>? = null,
    var city: String? = null,
    var latinCity: String? = null,
    var timezone: String? = null,
    var timezonesec: Int? = null,
    var latinCountry: String? = null,
    var id: Int? = null,
    var countryId: Int? = null,
    var score: Int? = null ): ListBaseItem, Serializable{

    companion object {
        const val SERIALIZABLE_NAME = "City"
    }
    override val baseItemType: BaseItemType
        get() = BaseItemType.AirportItem
}
