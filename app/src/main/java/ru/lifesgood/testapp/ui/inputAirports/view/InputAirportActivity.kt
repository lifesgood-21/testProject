package ru.lifesgood.testapp.ui.inputAirports.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding.widget.RxSearchView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_input_airport.*
import ru.lifesgood.testapp.core.PresentersProvider
import ru.lifesgood.testapp.core.network.NetworkRequestManager
import ru.lifesgood.testapp.core.network.data.City
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import com.transitionseverywhere.ChangeText
import ru.lifesgood.testapp.R
import ru.lifesgood.testapp.core.app.setVisibility
import ru.lifesgood.testapp.ui.inputAirports.presenter.InputAirportPresenter
import ru.lifesgood.testapp.ui.inputAirports.repository.InputAirportRepositoryImpl
import java.util.*

class InputAirportActivity : AppCompatActivity(R.layout.activity_input_airport), InputAirportView {

    companion object{
        const val REASON_EXTRA = "input_reason"

        fun open(fragment: Fragment, reason: InputReason){
            val intent = Intent(fragment.context, InputAirportActivity::class.java)
            intent.putExtra(REASON_EXTRA, reason.name)
            fragment.startActivityForResult(intent, reason.ordinal)
        }
    }

    private lateinit var searchView: android.widget.SearchView
    private val compositeDisposable = CompositeDisposable()
    private lateinit var presenter: InputAirportPresenter
    private val remoteRepository = InputAirportRepositoryImpl(NetworkRequestManager.api)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = PresentersProvider.getInputAirportPresenter(this, remoteRepository)
        presenter.onViewAttached(this)
        initViews()
        handleIntent(intent)
    }

    private fun initViews() {
        airportsRecyclerView.adapter = presenter.adapter
    }

    private fun handleIntent(intent: Intent){
        val reason = intent.getStringExtra(REASON_EXTRA)
        title = when (reason){
            InputReason.Arrival.name -> getString(
                R.string.arrival
            )
            InputReason.Departure.name -> getString(
                R.string.departure
            )
            else -> throw (Throwable("Unknown input reason"))
        }
    }

    override fun displayProgress(visibility: Boolean){
        searchProgress.setVisibility(visibility)
    }

    override fun displayErr(throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun displayEmptyListHint(visibility: Boolean){
        noDataHint.setVisibility(visibility)
        val transition = ChangeText()
        transition.changeBehavior = ChangeText.CHANGE_BEHAVIOR_OUT_IN
        TransitionManager.beginDelayedTransition(root, transition)
        noDataHint.text = getString(
            when(searchView.query.isEmpty()) {
                true -> R.string.enter_airport_name
                else -> R.string.airporn_not_find
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        initSearchView(searchItem)
        return true
    }

    private fun initSearchView(searchItem: MenuItem?) {
        this.searchView = searchItem?.actionView as android.widget.SearchView
        searchView.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        RxSearchView.queryTextChanges(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({presenter.findAirports(it, langCode())},{})
        searchView.setQuery(presenter.searchText, false)
        searchView.isIconified = false
    }

    @Suppress("DEPRECATION")
    private fun langCode(): String{
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val ims = imm.currentInputMethodSubtype
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (ims.languageTag.isEmpty()){
                true -> Locale.getDefault().language
                else -> ims.languageTag
            }
        }
        else{
            when (ims.locale.isEmpty()){
                true -> Locale.getDefault().language
                else -> ims.locale
            }
        }
    }

    override fun airportSelected(city: City) {
        val intent = Intent()
        intent.putExtra(City.SERIALIZABLE_NAME, city)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
        if(isFinishing){
            presenter.onDestroy()
            PresentersProvider.killInputAirportPresenter()
        }
    }

    enum class InputReason{
        Departure,
        Arrival
    }
}
