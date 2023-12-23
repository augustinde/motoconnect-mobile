package fr.motoconnect.architecture

import fr.motoconnect.data.repository.WeatherRepository
import fr.motoconnect.data.repository.WeatherRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val weatherRepository: WeatherRepository
}

class DefaultAppContainer: AppContainer {

    private val baseUrl = "http://api.weatherapi.com/v1/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitWeatherEndpoint: WeatherEndpoint by lazy {
        retrofit.create(WeatherEndpoint::class.java)
    }

    override val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(retrofitWeatherEndpoint)
    }
}