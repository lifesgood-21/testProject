package ru.lifesgood.testapp.core

import android.content.Context
import ru.lifesgood.testapp.ui.inputAirports.presenter.InputAirportPresenter
import ru.lifesgood.testapp.ui.inputAirports.repository.InputAirportRepository
import ru.lifesgood.testapp.ui.inputAirports.view.InputAirportView
import ru.lifesgood.testapp.ui.main.MainPresenter
import ru.lifesgood.testapp.ui.main.MainRemoteRepository
import ru.lifesgood.testapp.ui.main.MainView
import ru.lifesgood.testapp.ui.search.presenter.SearchPresenter
import ru.lifesgood.testapp.ui.search.view.SearchView

object PresentersProvider{

    private var mainPresenter: MainPresenter? = null
    private var searchPresenter: SearchPresenter? = null
    private var inputAirportPresenter: InputAirportPresenter? = null

    fun getMainPresenter(view: MainView? = null, repository: MainRemoteRepository,
                         applicationContext: Context): MainPresenter {
        if (mainPresenter == null)
            initMainPresenter(view, repository, applicationContext)
        return mainPresenter!!
    }

    private fun initMainPresenter(
        view: MainView?,
        repository: MainRemoteRepository,
        applicationContext: Context
    ) {
        mainPresenter = MainPresenter(view, repository, applicationContext)
    }

    fun killMainPresenter(){
        mainPresenter = null
    }

    fun getInputAirportPresenter(view: InputAirportView?, repository: InputAirportRepository):
            InputAirportPresenter {
        if (inputAirportPresenter == null)
            initInputAirportPresenter(view, repository)
        return inputAirportPresenter!!
    }

    private fun initInputAirportPresenter(view: InputAirportView?, repository: InputAirportRepository) {
        inputAirportPresenter = InputAirportPresenter(view, repository)
    }

    fun killInputAirportPresenter(){
        inputAirportPresenter = null
    }

    fun getSearchPresenter(view: SearchView?): SearchPresenter {
        if (searchPresenter == null)
            initSearchPresenter(view)
        return searchPresenter!!
    }

    private fun initSearchPresenter(view: SearchView?) {
        searchPresenter = SearchPresenter(view)
    }

    fun killSearchPresenter(){
        searchPresenter = null
    }
}

