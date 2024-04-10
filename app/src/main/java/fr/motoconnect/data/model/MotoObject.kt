package fr.motoconnect.data.model

data class MotoObject(
    val name: String? = null,
    var id: String? = null,
    val brakeFluid: Int? = 0,
    val chainLubrication: Int? = 0,
    val current: Boolean? = false,
    val engineOil: Int? = 0,
    val distance: Int? = 0,
    val totalJourney: Int? = 0,
    val frontTyreWear: Int? = 0,
    val rearTyreWear: Int? = 0,
)

enum class BaseDistance(val distance: Int) {
    BRAKE_FLUID(20000),
    CHAIN_LUBRICATION(250),
    ENGINE_OIL(6000),
    FRONT_TYRE(12000),
    REAR_TYRE(10000),
}