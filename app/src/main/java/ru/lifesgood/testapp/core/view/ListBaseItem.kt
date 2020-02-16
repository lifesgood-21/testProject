package ru.lifesgood.testapp.core.view

interface ListBaseItem {
    val baseItemType: BaseItemType

}

enum class BaseItemType{
    AirportItem,
    HeaderItem
}
