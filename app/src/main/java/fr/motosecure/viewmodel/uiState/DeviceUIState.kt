package fr.motosecure.viewmodel.uiState

import fr.motosecure.data.model.DeviceObject

data class DeviceUISate(
    val isPaired: Boolean = false,
    val isUnpaired: Boolean = false,
    val errorMsg: String = "",
    val device: DeviceObject? = null
)
