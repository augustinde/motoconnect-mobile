package fr.motoconnect.ui.screen.moto.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.viewmodel.uiState.JourneyUIState
import fr.motoconnect.viewmodel.uiState.MotoUIState

@Composable
fun MotoJourneysComponent(
    journeyUIState: JourneyUIState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MotoJourneysCard(
            stats = journeyUIState.journeyCount.toString(),
            text = stringResource(R.string.moto_number_of_journeys),
            60,
            0
        )
        /*MotoJourneysCard(
            stats = "4 km",
            text = stringResource(R.string.moto_total_distance),
            0,
            10
        )*/
        Spacer(modifier = Modifier.padding(0.dp, 16.dp))
    }
}

@Composable
fun MotoJourneysCard(stats: String, text: String, gap: Int, gap2: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.map_background),
                contentDescription = "Background Icon",
                alignment = Alignment.Center,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 70.dp, 0.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(0.dp, 0.dp, gap2.dp, 0.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stats,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(0.dp, 0.dp, gap.dp, 0.dp)
                )
            }
        }
    }
}
