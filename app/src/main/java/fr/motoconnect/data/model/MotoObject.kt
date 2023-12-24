package fr.motoconnect.data.model

data class MotoObject(
    val name: String? = null,
    val breakFluid: Int? = 20000,
    val chainLubrication: Int? = 250,
    val current: Boolean? = false,
    val engineOil: Int? = 6000,
    val distance: Int? = 0,
    val totalJourney: Int? = 0,
)