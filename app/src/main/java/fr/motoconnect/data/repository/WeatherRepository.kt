package fr.motoconnect.data.repository

import fr.motoconnect.BuildConfig
import fr.motoconnect.architecture.WeatherEndpoint
import fr.motoconnect.data.model.WeatherDto

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, long: Double): WeatherDto
}

class WeatherRepositoryImpl(
    private val weatherEndpoint: WeatherEndpoint
): WeatherRepository {

    private val apiKey: String = BuildConfig.WEATHER_API_KEY

    override suspend fun getCurrentWeather(lat: Double, long: Double): WeatherDto {
        val coords = "$lat,$long"
        try {
            return weatherEndpoint.getCurrentWeather(apiKey, coords)
        } catch (e: Exception) {
            throw e
        }
    }
}