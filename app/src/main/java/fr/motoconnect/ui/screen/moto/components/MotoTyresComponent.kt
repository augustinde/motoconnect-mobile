package fr.motoconnect.ui.screen.moto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.data.model.BaseDistance
import fr.motoconnect.viewmodel.uiState.MotoUIState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import fr.motoconnect.data.utils.NotificationService

@Composable
fun MotoTyresComponent(
    motoUIState: MotoUIState,
    resetFrontTyre: () -> Unit,
    resetRearTyre: () -> Unit,
) {

    val frontTyreWearPercentage = motoUIState.moto?.frontTyreWear?.toFloat()?.div(BaseDistance.FRONT_TYRE.distance.toFloat()) ?: 0f
    val rearTyreWearPercentage = motoUIState.moto?.rearTyreWear?.toFloat()?.div(BaseDistance.REAR_TYRE.distance.toFloat()) ?: 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.tertiary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ), modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.etat_des_roues),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(15.dp, 15.dp, 0.dp, 0.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center) {
                    MotoTyresCard(
                        tyreWearPercentage = frontTyreWearPercentage,
                        reset = resetFrontTyre,
                        wheelPosition = stringResource(
                            R.string.front_wheel
                        ),
                        limit = "${motoUIState.moto?.frontTyreWear}/${BaseDistance.FRONT_TYRE.distance} km"
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    MotoTyresCard(
                        tyreWearPercentage = rearTyreWearPercentage,
                        reset = resetRearTyre,
                        wheelPosition = stringResource(
                            R.string.rear_wheel
                        ),
                        limit = "${motoUIState.moto?.rearTyreWear}/${BaseDistance.REAR_TYRE.distance} km"
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun MotoTyresCard(
    tyreWearPercentage: Float,
    reset: () -> Unit,
    wheelPosition: String,
    limit: String
) {
    Column {
        Text(
            text = wheelPosition,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(25.dp, 0.dp, 0.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        CircularProgressMotoTyres(tyreWearPercentage = tyreWearPercentage)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { reset() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 10.dp)
        ) {
            Text(text = stringResource(R.string.moto_reset))
        }
        Text(
            text = limit,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 20.sp,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
fun CircularProgressMotoTyres(tyreWearPercentage: Float) {
    val color = when {
        tyreWearPercentage >= 1f -> colorResource(id = R.color.red)
        tyreWearPercentage > 2f / 3 -> colorResource(id = R.color.orange)
        tyreWearPercentage > 1f / 3 -> colorResource(id = R.color.yellow)
        else -> MaterialTheme.colorScheme.secondary
    }

    CircularProgressIndicator(
        progress = if (tyreWearPercentage > 1f) 1f else tyreWearPercentage,
        color = color,
        strokeWidth = 5.dp,
        modifier = Modifier.padding(50.dp, 0.dp, 0.dp, 0.dp)
    )
}

@Composable
fun MotoTyresNotifications(tyreWearPercentage: Float, wheelPosition: String, notificationID : Int){


    val title = stringResource(R.string.alert_tyre_wear)
    val msgDanger = stringResource(R.string.danger_level, wheelPosition)
    val msgWarning = stringResource(R.string.warning_level, wheelPosition)

    val notificationService = NotificationService(LocalContext.current)

    if (tyreWearPercentage >= 1f) {
        notificationService.sendNotification(title, msgDanger, notificationID) {
            notificationService.getActiveNotification(notificationID)
        }
    } else if (tyreWearPercentage > 2f / 3) {
        notificationService.sendNotification(title, msgWarning, notificationID){
            notificationService.getActiveNotification(notificationID)
        }
    }
}