package ru.lifesgood.testapp.ui.search.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import ru.lifesgood.testapp.R
import ru.lifesgood.testapp.core.PresentersProvider
import ru.lifesgood.testapp.core.network.data.City
import ru.lifesgood.testapp.ui.search.presenter.SearchPresenter

class SearchFragment : Fragment(R.layout.fragment_search), SearchView {

    companion object{
        const val DEPARTURE_KEY = "departure"
        const val ARRIVAL_KEY = "arrival"
    }

    private lateinit var presenter: SearchPresenter
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = PresentersProvider.getSearchPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewAttach(this)
        presenter.departure = arguments?.getSerializable(DEPARTURE_KEY) as City
        presenter.arrival = arguments?.getSerializable(ARRIVAL_KEY) as City
        initViews()
    }

    private fun initViews() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(presenter)
    }

    override fun getIntent() = activity?.intent

    override fun showError(throwable: Throwable) {
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        PresentersProvider.killSearchPresenter()
    }
}
