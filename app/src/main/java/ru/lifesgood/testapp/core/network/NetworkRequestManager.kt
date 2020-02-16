package ru.lifesgood.testapp.core.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkRequestManager {

    private const val BASE_URL = "https://yasen.hotellook.com"

    private var gsonConverter: GsonBuilder = GsonBuilder()
        .serializeNulls()
        .setDateFormat("dd.MM.yyyy HH:mm:ss")
        .setLenient()

    private var client: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    var api: AppApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gsonConverter.create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()
        .create(AppApi::class.java)
}