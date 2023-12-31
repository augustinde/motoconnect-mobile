package fr.motoconnect.viewmodel.uiState

import fr.motoconnect.data.model.MotoObject

data class MotoUIState(
    val moto: MotoObject? = null,
    val errorMsg: String? = null,
)