package ru.lifesgood.testapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.transition.TransitionManager
import com.transitionseverywhere.ChangeText
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.root
import ru.lifesgood.testapp.R
import ru.lifesgood.testapp.core.PresentersProvider
import ru.lifesgood.testapp.core.network.NetworkRequestManager
import ru.lifesgood.testapp.core.network.data.City
import ru.lifesgood.testapp.ui.inputAirports.view.InputAirportActivity
import ru.lifesgood.testapp.ui.search.view.SearchFragment.Companion.ARRIVAL_KEY
import ru.lifesgood.testapp.ui.search.view.SearchFragment.Companion.DEPARTURE_KEY

class MainFragment : Fragment(R.layout.fragment_main), MainView {

    private lateinit var presenter: MainPresenter
    private val repository = MainRemoteRepositoryImpl(NetworkRequestManager.api)
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        retainInstance = true
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        presenter = PresentersProvider.getMainPresenter(this, repository, context?.applicationContext!!)
        presenter.onViewCreated(this)
        initViews()
    }

    private fun initViews() {
        arrival.setOnClickListener {
            InputAirportActivity.open(this, InputAirportActivity.InputReason.Arrival)
        }
        departure.setOnClickListener {
            InputAirportActivity.open(this, InputAirportActivity.InputReason.Departure)
        }
        findButton.setOnClickListener {presenter.startSearch()}
        changePoints.setOnClickListener { changeArrivalVsDepartureText() }
    }

    private fun changeArrivalVsDepartureText() {
        presenter.changeArrivalVsDeparture()
    }

    override fun displayError(throwable: Throwable) {
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun displayArrival(arrival: City?) {
        val effect = ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN)
        TransitionManager.beginDelayedTransition(root, effect)
        this.arrival.text = arrival?.city?:""
    }

    override fun displayDeparture(departure: City?) {
        val effect = ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN)
        TransitionManager.beginDelayedTransition(root, effect)
        this.departure.text = departure?.city?:""
    }

    override fun startSearch(departure: City, arrival: City){
        val bundle = Bundle()
        bundle.putSerializable(ARRIVAL_KEY, arrival)
        bundle.putSerializable(DEPARTURE_KEY, departure)
        navController.navigate(R.id.action_mainFragment_to_searchFragment, bundle)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            InputAirportActivity.InputReason.Departure.ordinal -> {
                presenter.handleDepartureInput(resultCode, data)
            }
            InputAirportActivity.InputReason.Arrival.ordinal -> {
                presenter.handleArrivalInput(resultCode, data)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PresentersProvider.killMainPresenter()
    }
}
