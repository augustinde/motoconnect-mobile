package fr.motoconnect.ui.screen.journey.journeyList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.motoconnect.R
import fr.motoconnect.data.model.JourneyObject
import fr.motoconnect.data.utils.TimeUtils

@Composable
fun JourneyCardComponent(
    journeyObject: JourneyObject,
    onClickSeeMore: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(0.dp, 0.dp, 16.dp, 0.dp),
        ) {
            Text(
                TimeUtils().toDateTimeString(journeyObject.startDateTime!!),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
            )
            Text(
                stringResource(R.string.distance_km, journeyObject.distance!!),
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                stringResource(R.string.duration, TimeUtils().toHourMinute(journeyObject.duration!!)),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Button(
            modifier = Modifier
                .weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.tertiary,
            ),
            onClick = {
                onClickSeeMore()
            }
        ) {
            Text(stringResource(R.string.see_more))
        }
    }
}