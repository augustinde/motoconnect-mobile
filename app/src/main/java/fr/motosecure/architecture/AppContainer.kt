package fr.motosecure.architecture

import fr.motosecure.architecture.endpoints.GeocodingEndpoint
import fr.motosecure.data.repository.GeocodingRepository
import fr.motosecure.data.repository.GeocodingRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val geocodingRepository: GeocodingRepository
}

class DefaultAppContainer: AppContainer {

    private val baseUrlGeocoding = "https://api.mapbox.com/geocoding/v5/mapbox.places/"

    private val retrofitGeocoding: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrlGeocoding)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val retrofitGeocodingEndpoint: GeocodingEndpoint by lazy {
        retrofitGeocoding.create(GeocodingEndpoint::class.java)
    }

    override val geocodingRepository: GeocodingRepository by lazy {
        GeocodingRepositoryImpl(retrofitGeocodingEndpoint)
    }
}