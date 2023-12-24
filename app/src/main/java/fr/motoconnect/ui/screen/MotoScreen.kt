package fr.motoconnect.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.motoconnect.R
import fr.motoconnect.viewmodel.MotoViewModel

@Composable
fun MotoScreen(
){
    val motoViewModel: MotoViewModel = viewModel()
    val motoUIState = motoViewModel.motoUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
    ) {
        if(motoUIState.value.moto == null){
            CreateMotoComponent(
                motoViewModel = motoViewModel,
            )
        } else {
            Text(text = "ListMotocomponent")
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMotoComponent(
    motoViewModel: MotoViewModel,
){

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

        if(motoUIState.errorMsg != null){
            Text(text = motoUIState.errorMsg!!)
        }
    }
}