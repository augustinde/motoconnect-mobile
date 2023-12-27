package fr.motoconnect.data.model

data class MotoObject(
    val name: String? = null,
    val breakFluid: Int? = 0,
    val chainLubrication: Int? = 0,
    val current: Boolean? = false,
    val engineOil: Int? = 0,
    val distance: Int? = 0,
    val totalJourney: Int? = 0,
)

enum class BaseDistance(val distance: Int) {
    BREAK_FLUID(20000),
    CHAIN_LUBRICATION(250),
    ENGINE_OIL(6000),
}