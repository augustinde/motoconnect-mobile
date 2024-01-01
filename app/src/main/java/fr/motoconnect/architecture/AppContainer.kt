package fr.motoconnect.architecture

import fr.motoconnect.architecture.endpoints.GeocodingEndpoint
import fr.motoconnect.architecture.endpoints.WeatherEndpoint
import fr.motoconnect.data.repository.GeocodingRepository
import fr.motoconnect.data.repository.GeocodingRepositoryImpl
import fr.motoconnect.data.repository.WeatherRepository
import fr.motoconnect.data.repository.WeatherRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val weatherRepository: WeatherRepository
    val geocodingRepository: GeocodingRepository
}

class DefaultAppContainer: AppContainer {

    private val baseUrlWeather = "http://api.weatherapi.com/v1/"
    private val baseUrlGeocoding = "https://api.mapbox.com/geocoding/v5/mapbox.places/"

    private val retrofitWeather: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrlWeather)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitGeocoding: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrlGeocoding)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitWeatherEndpoint: WeatherEndpoint by lazy {
        retrofitWeather.create(WeatherEndpoint::class.java)
    }

    private val retrofitGeocodingEndpoint: GeocodingEndpoint by lazy {
        retrofitGeocoding.create(GeocodingEndpoint::class.java)
    }

    override val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(retrofitWeatherEndpoint)
    }

    override val geocodingRepository: GeocodingRepository by lazy {
        GeocodingRepositoryImpl(retrofitGeocodingEndpoint)
    }
}