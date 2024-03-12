package fr.motoconnect.viewmodel.uiState

import fr.motoconnect.data.model.DeviceObject

data class DeviceUISate(
    val isPaired: Boolean = false,
    val isUnpaired: Boolean = false,
    val errorMsg: String = "",
    val device: DeviceObject? = null
)
