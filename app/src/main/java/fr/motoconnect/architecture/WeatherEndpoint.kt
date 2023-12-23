package fr.motoconnect.architecture

import fr.motoconnect.data.model.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherEndpoint {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query(value = "key") key: String,
        @Query(value = "q") coords: String,
    ): WeatherDto

}