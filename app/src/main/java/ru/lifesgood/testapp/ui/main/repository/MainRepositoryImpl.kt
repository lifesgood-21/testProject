package ru.lifesgood.testapp.ui.main.repository

import ru.lifesgood.testapp.core.network.AppApi
import ru.lifesgood.testapp.ui.main.repository.MainRemoteRepository

class MainRemoteRepositoryImpl(private val api: AppApi):
    MainRemoteRepository