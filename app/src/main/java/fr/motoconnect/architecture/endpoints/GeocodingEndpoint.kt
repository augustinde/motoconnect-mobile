package fr.motoconnect.architecture.endpoints

import fr.motoconnect.data.model.GeocodingDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GeocodingEndpoint {

    @GET("{longitude},{latitude}.json")
    suspend fun reverseGeocoding(
        @Path(value = "longitude") longitude: Double,
        @Path(value = "latitude") latitude: Double,
        @Query(value = "access_token") key: String,
        @Query(value = "language") language: String,
    ): GeocodingDto
}