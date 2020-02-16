package ru.lifesgood.testapp.ui.inputAirports.view

import android.view.View
import kotlinx.android.synthetic.main.item_airport.view.*
import ru.lifesgood.testapp.core.network.data.City
import ru.lifesgood.testapp.core.view.BaseItemClickListener
import ru.lifesgood.testapp.core.view.BaseViewHolder

class AirportViewHolder(val view: View, private val clickListener: BaseItemClickListener?): BaseViewHolder<City>(view) {

    override fun bindTo(data: City) {
        with(view){
            setOnClickListener { clickListener?.onBaseItemClick(data, this) }
            city.text = data.city
            code.text = data.countryCode
        }
    }
}