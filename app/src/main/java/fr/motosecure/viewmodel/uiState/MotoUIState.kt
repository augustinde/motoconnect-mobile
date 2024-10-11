package fr.motosecure.viewmodel.uiState

import fr.motosecure.data.model.MotoObject

data class MotoUIState(
    val moto: MotoObject? = null,
    val errorMsg: String? = null,
)