package ru.lifesgood.testapp.core.view

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.lifesgood.testapp.R
import ru.lifesgood.testapp.ui.inputAirports.view.AirportViewHolder

@Suppress("UNCHECKED_CAST")
object ListItemAdapterFactory {

    fun get(parent: ViewGroup, viewType: Int, clickListener: BaseItemClickListener): BaseViewHolder<ListBaseItem> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_airport, parent, false)
        return when(viewType){
            BaseItemType.AirportItem.ordinal -> AirportViewHolder(
                view,
                clickListener
            )
            else -> throw (Throwable("Unknown ListItem"))
        }as BaseViewHolder<ListBaseItem>
    }

}
