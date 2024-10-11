package fr.motosecure.data.model

data class GeocodingDto(
    val features: List<FeatureDto>
)

data class FeatureDto(
    val text: String,
    val id : String,
)