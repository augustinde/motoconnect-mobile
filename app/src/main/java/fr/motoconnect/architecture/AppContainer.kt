package fr.motoconnect.architecture

import fr.motoconnect.architecture.endpoints.WeatherEndpoint
import fr.motoconnect.data.repository.WeatherRepository
import fr.motoconnect.data.repository.WeatherRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val weatherRepository: WeatherRepository
}

class DefaultAppContainer: AppContainer {

    private val baseUrlWeather = "http://api.weatherapi.com/v1/"

    private val retrofitWeather: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrlWeather)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitWeatherEndpoint: WeatherEndpoint by lazy {
        retrofitWeather.create(WeatherEndpoint::class.java)
    }

    override val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(retrofitWeatherEndpoint)
    }
}