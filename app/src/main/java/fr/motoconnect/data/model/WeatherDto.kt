package fr.motoconnect.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("current")
    @Expose
    val current: CurrentDto
)

data class CurrentDto(
    @SerializedName("temp_c")
    @Expose
    val tempC: Double,
    @SerializedName("condition")
    @Expose
    val condition: ConditionDto
)

data class ConditionDto(
    @SerializedName("text")
    @Expose
    val text: String,
    @SerializedName("icon")
    @Expose
    val icon: String
)

