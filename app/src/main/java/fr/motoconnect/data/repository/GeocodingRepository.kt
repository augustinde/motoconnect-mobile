package fr.motoconnect.data.repository

import fr.motoconnect.BuildConfig
import fr.motoconnect.architecture.endpoints.GeocodingEndpoint
import fr.motoconnect.data.model.GeocodingDto
import java.util.Locale

interface GeocodingRepository {
    suspend fun getCity(lat: Double, long: Double): String
}

class GeocodingRepositoryImpl(
    private val geocodingEndpoint: GeocodingEndpoint
) : GeocodingRepository {

    private val apiKey: String = BuildConfig.GEOCODING_API_KEY

    override suspend fun getCity(lat: Double, long: Double): String {
        try {
            val geocodingResponse: GeocodingDto =
                geocodingEndpoint.reverseGeocoding(long, lat, apiKey, Locale.getDefault().language)
            return geocodingResponse.features
                .firstOrNull { it.id.contains("place") }
                ?.text.orEmpty()
        } catch (e: Exception) {
            throw e
        }
    }
}