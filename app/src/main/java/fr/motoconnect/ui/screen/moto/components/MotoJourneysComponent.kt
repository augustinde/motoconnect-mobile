package fr.motoconnect.ui.screen.moto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.viewmodel.uiState.MotoUIState

@Composable
fun MotoJourneysComponent(
    motoUIState: MotoUIState,
) {
    Box(
        modifier = Modifier
            .padding(0.dp, 16.dp)
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            Text(
                text = stringResource(R.string.moto_journeys),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
            )

            Row {
                Text(
                    text = stringResource(R.string.moto_number_of_journeys),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "${motoUIState.moto?.totalJourney}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Row {
                Text(
                    text = stringResource(R.string.moto_total_distance),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "${motoUIState.moto?.distance} km",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}
