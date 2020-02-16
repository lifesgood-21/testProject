package ru.lifesgood.testapp.core.view

import androidx.recyclerview.widget.DiffUtil

class ListBaseItemDiffCallback(
    private val oldItems: List<ListBaseItem>,
    private val newItems: List<ListBaseItem>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.baseItemType == newItem.baseItemType
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}