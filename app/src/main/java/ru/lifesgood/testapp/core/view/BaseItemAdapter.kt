package ru.lifesgood.testapp.core.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseItemAdapter(
    var items: List<ListBaseItem>,
    private val clickListener: BaseItemClickListener): RecyclerView.Adapter<BaseViewHolder<ListBaseItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ListBaseItem> {
        return ListItemAdapterFactory.get(parent, viewType, clickListener)
    }

    override fun getItemViewType(position: Int) = items[position].baseItemType.ordinal

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<ListBaseItem>, position: Int) {
        holder.bindTo(items[position])
    }
}