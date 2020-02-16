package ru.lifesgood.testapp.ui.search.view

import android.content.Intent

interface SearchView {

    fun showError(throwable: Throwable)

    fun getIntent(): Intent?

}