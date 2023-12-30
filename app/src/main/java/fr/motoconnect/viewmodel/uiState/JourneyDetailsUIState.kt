package fr.motoconnect.viewmodel.uiState

import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.model.JourneyPlayerState
import fr.motoconnect.data.model.PointObject

data class JourneyDetailsUIState(
    var isLoading: Boolean = true,
    var journey: JourneyObject? = null,
    var errorMsg: String? = null,
    val currentPoint: PointObject? = null,
    val playerState: JourneyPlayerState = JourneyPlayerState.STOPPED,
)