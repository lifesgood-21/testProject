package ru.lifesgood.testapp.ui.inputAirports.presenter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.lifesgood.testapp.core.network.data.City
import ru.lifesgood.testapp.core.view.BaseItemClickListener
import ru.lifesgood.testapp.core.view.ListBaseItem
import ru.lifesgood.testapp.core.view.ListBaseItemDiffCallback
import ru.lifesgood.testapp.core.view.BaseItemAdapter
import ru.lifesgood.testapp.ui.inputAirports.repository.InputAirportRepository
import ru.lifesgood.testapp.ui.inputAirports.view.InputAirportView

class InputAirportPresenter(var view: InputAirportView? = null,
                            private val repository: InputAirportRepository): BaseItemClickListener {

    var searchText: CharSequence = ""
    val adapter = BaseItemAdapter(emptyList(), this)
    private var searchAirportsDisposable: Disposable? = null

    fun onViewAttached(view: InputAirportView) {
        this.view = view
    }

    private fun handleFindedAirports(airports: List<City>?){
        view?.displayEmptyListHint(airports?.isNullOrEmpty()?:true)
        val callback = ListBaseItemDiffCallback(adapter.items, airports?:emptyList())
        val diff = DiffUtil.calculateDiff(callback)
        adapter.items = airports?:emptyList()
        diff.dispatchUpdatesTo(adapter)
    }

    override fun onBaseItemClick(item: ListBaseItem, view: View) {
        when(item){
            is City -> this.view?.airportSelected(item)
        }
    }

    fun findAirports(term: CharSequence?, language: String) {
        this.searchText = term.toString()
        if(searchAirportsDisposable?.isDisposed == false)
            searchAirportsDisposable?.dispose()
        searchAirportsDisposable = repository.getAirports(term.toString(), language)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view?.displayProgress(true)}
            .doFinally{ view?.displayProgress(false)}
            .subscribe({handleFindedAirports(it.cities)}, {view?.displayErr(it)})
    }

    fun onDestroy() {
        if(searchAirportsDisposable?:true == false)
            searchAirportsDisposable?.dispose()
    }



}
