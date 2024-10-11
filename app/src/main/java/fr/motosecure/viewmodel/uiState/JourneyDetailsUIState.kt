package fr.motosecure.viewmodel.uiState

import fr.motosecure.data.model.JourneyObject
import fr.motosecure.data.model.JourneyPlayerState
import fr.motosecure.data.model.PointObject

data class JourneyDetailsUIState(
    var isLoading: Boolean = true,
    var journey: JourneyObject? = null,
    var errorMsg: String? = null,
    val currentPoint: PointObject? = null,
    val playerState: JourneyPlayerState = JourneyPlayerState.STOPPED,
)