package fr.motoconnect.ui.screen.moto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.motoconnect.R
import fr.motoconnect.data.model.BaseDistance
import fr.motoconnect.viewmodel.uiState.MotoUIState

@Composable
fun MotoFluidsComponent(
    motoUIState: MotoUIState,
    resetEngineOil: () -> Unit,
    resetBreakFluid: () -> Unit,
    resetChainLubrication: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(0.dp, 16.dp)
            .clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Column {
            Text(
                text = stringResource(R.string.moto_maintenance),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.moto_engine_oil),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.width(120.dp)
                )
                Text(
                    text = "${motoUIState.moto?.engineOil}/${BaseDistance.ENGINE_OIL.distance} km",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp,
                )
                Button(
                    onClick = { resetEngineOil() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ){
                    Text(text = stringResource(R.string.moto_reset))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.moto_break_fluid),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.width(120.dp)
                )
                Text(
                    text = "${motoUIState.moto?.breakFluid}/${BaseDistance.BREAK_FLUID.distance} km",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Button(
                    onClick = { resetBreakFluid() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ){
                    Text(text = stringResource(R.string.moto_reset))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.moto_chain_greasing),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.width(120.dp)
                )
                Text(
                    text = "${motoUIState.moto?.chainLubrication}/${BaseDistance.CHAIN_LUBRICATION.distance} km",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Button(
                    onClick = { resetChainLubrication() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ){
                    Text(text = stringResource(R.string.moto_reset))
                }
            }
        }
    }
}