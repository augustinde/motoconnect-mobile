package fr.motoconnect.viewmodel.uiState

import fr.motoconnect.data.model.DeviceObject
import fr.motoconnect.data.model.UserObject

data class AuthUIState(
    val user: UserObject? = null,
    val isLogged: Boolean = false,
    val errorMessage: String? = null,
    val device: DeviceObject? = null,
)