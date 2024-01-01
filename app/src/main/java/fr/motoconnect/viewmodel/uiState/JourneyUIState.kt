package fr.motoconnect.viewmodel.uiState

import fr.motoconnect.data.model.JourneyObject

data class JourneyUIState(
    val journeys: List<JourneyObject> = emptyList(),
    val isLoading: Boolean = true,
    val errorMsg: String? = null,
)