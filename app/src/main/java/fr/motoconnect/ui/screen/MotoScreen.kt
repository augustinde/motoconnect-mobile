package fr.motoconnect.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.motoconnect.R
import fr.motoconnect.data.model.BaseDistance
import fr.motoconnect.viewmodel.MotoUIState
import fr.motoconnect.viewmodel.MotoViewModel

@Composable
fun MotoScreen(
) {
    val motoViewModel: MotoViewModel = viewModel()
    val motoUIState = motoViewModel.motoUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
    ) {
        if (motoUIState.value.moto == null) {
            CreateMotoComponent(
                motoViewModel = motoViewModel,
            )
        } else {
            MotoDetailsComponent(motoViewModel = motoViewModel)
        }
    }
}

@Composable
fun MotoDetailsComponent(
    motoViewModel: MotoViewModel,
) {

    val motoUIState by motoViewModel.motoUiState.collectAsState()
    LaunchedEffect(motoUIState.moto) {
        motoViewModel.getCurrentMoto()
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = motoUIState.moto?.name!!.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        MotoFluidsComponent(
            motoUIState = motoUIState,
            resetEngineOil = {
                motoViewModel.resetEngineOil()
            },
            resetBreakFluid = {
                motoViewModel.resetBreakFluid()
            },
            resetChainLubrication = {
                motoViewModel.resetChainLubrication()
            }
        )

        MotoJourneysComponent(motoUIState = motoUIState)

    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMotoComponent(
    motoViewModel: MotoViewModel,
) {

    val motoUIState by motoViewModel.motoUiState.collectAsState()

    var motoName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.add_moto_title),
            fontSize = 20.sp
        )
        TextField(
            value = motoName,
            onValueChange = { motoName = it },
            label = { Text(text = stringResource(id = R.string.name_moto_label)) },
            isError = motoUIState.errorMsg != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = {
                motoViewModel.createMoto(motoName)
            }),
        )
        Button(
            onClick = {
                motoViewModel.createMoto(motoName)
            }
        ) {
            Text(text = stringResource(id = R.string.add_button))
        }

        if (motoUIState.errorMsg != null) {
            Text(text = motoUIState.errorMsg!!)
        }
    }
}